package com.example.tobyspring.ex01;

import com.example.tobyspring.ch01.dao.DaoFactory;
import com.example.tobyspring.ch01.domain.User;
import com.example.tobyspring.ch01.dao.UserDao;
import com.example.tobyspring.ch05.Level;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;


import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat; // JUNIT 5 junit assertThat
import static org.hamcrest.Matchers.is;


@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
class UserDaoTest {


    private User user1;
    private User user2;
    private User user3;

    @Autowired
    private UserDao dao;

    @Autowired
    private ApplicationContext context;

    @BeforeEach
    void setUp() throws SQLException{
        this.dao = context.getBean("userDao", UserDao.class);
        dao.deleteAll();

        this.user1 = new User("gyumee", "박성철", "springno1", Level.BASIC, 1, 0);
        this.user2 = new User("leegw700", "이길원", "springno2", Level.SILVER, 55, 10);
        this.user3 = new User("bumjin", "박범진", "springno3", Level.GOLD, 100, 40);
    }

    @Test
    void addAndGet() throws SQLException {


        assertThat(dao.getCount(), is(0));


        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User userget1 = dao.get(user1.getId());
        checkSameUser(userget1, user1);
        assertThat(userget1.getName(), is(user1.getName()));
        assertThat(userget1.getPassword(), is(user1.getPassword()));

        User userget2 = dao.get(user2.getId());
        checkSameUser(userget2, user2);
        assertThat(userget2.getName(), is(user2.getName()));
        assertThat(userget2.getPassword(), is(user2.getPassword()));
    }


    @Test
    void count() throws SQLException {

        IntStream.range(1, 11).forEach(i -> {
            try {
                User user = new User("User" + i, "Name" + i, "Password" + i, Level.BASIC, 0, 0);
                dao.add(user);
                assertThat(dao.getCount(), is(i));
            } catch (BadSqlGrammarException e) {
                throw e;
            }
        });

    }

    @Test
    void getUserFailure() throws SQLException, ClassNotFoundException {

        assertThat(dao.getCount(), is(0));

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            dao.get("unknown_id");
        });
    }

    @Test
    void getAll() throws SQLException{
        List<User> users0 = dao.getAll();
        assertThat(users0.size(), is(0));

        dao.add(user1);
        List<User> users1 = dao.getAll();
        assertThat(users1.size(), is(1));
        checkSameUser(user1, users1.get(0));

        dao.add(user2);
        List<User> users2 = dao.getAll();
        assertThat(users2.size(), is(2));
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        dao.add(user3);
        List<User> users3 = dao.getAll();
        assertThat(users3.size(), is(3));
        checkSameUser(user3, users3.get(0));
        checkSameUser(user1, users3.get(1));
        checkSameUser(user2, users3.get(2));
    }

    private void checkSameUser(User user1, User user2) {
        org.assertj.core.api.Assertions.assertThat((user1.getId())).isEqualTo(user2.getId());
        org.assertj.core.api.Assertions.assertThat((user1.getName())).isEqualTo(user2.getName());
        org.assertj.core.api.Assertions.assertThat((user1.getPassword())).isEqualTo(user2.getPassword());
        org.assertj.core.api.Assertions.assertThat((user1.getLevel())).isEqualTo(user2.getLevel());
        org.assertj.core.api.Assertions.assertThat((user1.getLogin())).isEqualTo(user2.getLogin());
        org.assertj.core.api.Assertions.assertThat((user1.getRecommend())).isEqualTo(user2.getRecommend());
    }

    @Test
    public void update() {

        dao.add(user1);
        dao.add(user2);

        user1.setName("오민규");
        user1.setPassword("springno6");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        dao.update(user1);

        User user1update = dao.get(user1.getId());
        checkSameUser(user1, user1update);
        User user2same = dao.get(user2.getId());
        checkSameUser(user2, user2same);
    }


//    @Test
//    public void duplicateKey() {
//        dao.add(user1);
//        dao.add(user1);
//        Assertions.assertThrows(DataAccessException.class, () -> {
//            dao.add(user1);
//        });
//    }
}