package com.example.tobyspring.ex01;

import com.example.tobyspring.ch01.dao.DaoFactory;
import com.example.tobyspring.ch01.domain.User;
import com.example.tobyspring.ch01.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;
import java.util.stream.IntStream;


import static org.hamcrest.MatcherAssert.assertThat; // JUNIT 5 junit assertThat
import static org.hamcrest.Matchers.is;


class UserDaoTest {

    @Test
    void addAndGet() throws SQLException {

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);
        User user1 = new User("gyumee", "박성철", "springno1");
        User user2 = new User("leegw700", "이길원", "springno2");


        dao.deleteAll();
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
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

        dao.deleteAll();
        IntStream.range(1, 11).forEach(i -> {
            try {
                User user = new User("User" + i, "Name" + i, "Password" + i);
                dao.add(user);
                assertThat(dao.getCount(), is(i));
            } catch (SQLException e) {

            }
        });
    }
}