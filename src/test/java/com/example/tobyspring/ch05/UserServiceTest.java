package com.example.tobyspring.ch05;

import com.example.tobyspring.ch01.dao.UserDao;
import com.example.tobyspring.ch01.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static com.example.tobyspring.ch05.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static com.example.tobyspring.ch05.UserService.MIN_RECOMMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    MailSender mailSender;


    List<User> users;
    User user;

    @BeforeEach
    void setUp() {
        userDao.deleteAll();
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0, "useradmin@ksug.org"),
                new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0, "acepade231@gmail.com"),
                new User("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1, "useradmin@ksug.org"),
                new User("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "useradmin@ksug.org"),
                new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "useradmin@ksug.org"));
        user = new User();
    }

    @Test
    void bean() {
        assertThat(userService).isNotNull();
    }

    @Test
    @DirtiesContext
    void upgradeLevels() throws Exception {
        for (User user : users) {
            userDao.add(user);
        }

        MockMailSender mockMailSender = new MockMailSender();
        userService.setMailSender(mockMailSender);

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);

        List<String> request = mockMailSender.getRequests();
        assertThat(request.size()).isEqualTo(2);
        assertThat(request.get(0)).isEqualTo(users.get(1).getEmail());
        assertThat(request.get(1)).isEqualTo(users.get(3).getEmail());
    }


    @Test
    void add() {

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(userWithoutLevelRead.getLevel()).isEqualTo(userWithoutLevel.getLevel());

    }

    @Test
    void cannotUpgradeLevel() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if (level.nextLevel() != null) continue;
            user.setLevel(level);
            assertThrows(IllegalArgumentException.class, () -> {
                user.upgradeLevel();
            });
        }
    }

    @Test
    public void upgradeAllOrNothing() throws Exception {
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(userDao);
        testUserService.setTransactionManager(transactionManager);
        testUserService.setMailSender(mailSender);
        for(User user : users) userDao.add(user);

        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        }
        catch(TestUserServiceException e) {
        }

        checkLevelUpgraded(users.get(1), false);
    }

    private void checkLevel(User user, Level expectedLevel) {

        User userUpdate = userDao.get(user.getId());
        assertThat(userUpdate.getLevel()).isEqualTo(expectedLevel);

    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        }
        else {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }
    }

    static class TestUserService extends UserService {
        private String id;

        private TestUserService(String id) {
            this.id = id;
        }

        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException{
    }

    static class MockMailSender implements MailSender {
        private List<String> requests = new ArrayList<>();

        public List<String> getRequests() {
            return requests;
        }

        public void send(SimpleMailMessage mailMessage) throws MailException {
            requests.add(mailMessage.getTo()[0]);
        }

        public void send(SimpleMailMessage[] mailMessages) throws MailException {

        }
    }
}

