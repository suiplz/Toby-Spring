//package com.example.tobyspring.ch06;

import com.example.tobyspring.ch01.domain.User;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

//public class UserServiceTx implements UserService{
//    UserService userService;
//    PlatformTransactionManager transactionManager;
//
//    public void setUserService(UserService userService) {
//        this.userService = userService;
//    }
//
//    public void setTransactionManager(PlatformTransactionManager transactionManager) {
//        this.transactionManager = transactionManager;
//    }
//
//    public void add(User user){
//        this.userService.add(user);
//    }
//
//    public void upgradeLevels() {
//        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
//        try {
//            userService.upgradeLevels();
//            this.transactionManager.commit(status);
//        } catch (RuntimeException e) {
//            this.transactionManager.rollback(status);
//            throw e;
//        }
//    }
//}
