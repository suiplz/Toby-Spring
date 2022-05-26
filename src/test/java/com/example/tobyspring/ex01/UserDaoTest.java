package com.example.tobyspring.ex01;

import com.example.tobyspring.ch01.dao.DaoFactory;
import com.example.tobyspring.ch01.domain.User;
import com.example.tobyspring.ch01.dao.UserDao;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
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

        this.user1 = new User("gyumee", "박성철", "springno1");
        this.user2 = new User("leegw700", "이길원", "springno2");
        this.user3 = new User("bumjin", "박범진", "springno3");
    }

    @Test
    void addAndGet() throws SQLException {


        assertThat(dao.getCount(), is(0));


        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User userget1 = dao.get(user1.getId());
        assertThat(userget1.getName(), is(user1.getName()));
        assertThat(userget1.getPassword(), is(user1.getPassword()));

        User userget2 = dao.get(user2.getId());
        assertThat(userget2.getName(), is(user2.getName()));
        assertThat(userget2.getPassword(), is(user2.getPassword()));
    }

    @Test
    void identityTest(){
        DaoFactory factory = new DaoFactory();

        UserDao dao1 = factory.userDao();
        UserDao dao2 = factory.userDao();

        System.out.println("dao1 = " + dao1);
        System.out.println("dao2 = " + dao2);

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

        UserDao dao3 = context.getBean("userDao", UserDao.class);
        UserDao dao4 = context.getBean("userDao", UserDao.class);

        System.out.println("dao3 = " + dao3);
        System.out.println("dao4 = " + dao4);

        System.out.println(dao1==dao2);
        System.out.println(dao3==dao4);

        System.out.println(dao1.equals(dao2));
        System.out.println(dao4.equals(dao3));


    }

    @Test
    void count() throws SQLException {

        IntStream.range(1, 11).forEach(i -> {
            try {
                User user = new User("User" + i, "Name" + i, "Password" + i);
                dao.add(user);
                assertThat(dao.getCount(), is(i));
            } catch (SQLException e) {

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
    }
}