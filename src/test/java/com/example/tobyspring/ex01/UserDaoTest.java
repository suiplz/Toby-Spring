package com.example.tobyspring.ex01;

import com.example.tobyspring.ch01.dao.DaoFactory;
import com.example.tobyspring.ch01.domain.User;
import com.example.tobyspring.ch01.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

class UserDaoTest {

    public static void main(String[] args) throws ClassNotFoundException, SQLException{

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("whiteShip");
        user.setName("백기선");
        user.setPassword("123");

        dao.add(user);

        System.out.println(user.getId() + "등록 성공");

        User user2 = dao.get(user.getId());

        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + "조회 성공");
    }

    @Test
    public void identityTest(){
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
}