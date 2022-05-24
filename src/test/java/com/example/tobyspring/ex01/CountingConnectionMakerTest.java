package com.example.tobyspring.ex01;

import com.example.tobyspring.ch01.dao.CountingConnectionMaker;
import com.example.tobyspring.ch01.dao.CountingDaoFactory;
import com.example.tobyspring.ch01.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class CountingConnectionMakerTest {

    @Test
    public void CountingTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

        CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
        System.out.println("ccm = " + ccm.getCounter());
    }

}