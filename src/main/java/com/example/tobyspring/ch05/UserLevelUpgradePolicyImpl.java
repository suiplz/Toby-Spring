package com.example.tobyspring.ch05;

import com.example.tobyspring.ch01.dao.UserDao;
import com.example.tobyspring.ch01.domain.User;

//public class UserLevelUpgradePolicyImpl implements UserLevelUpgradePolicy{


//    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
//    public static final int MIN_RECOMMEND_FOR_GOLD = 30;
//
//    public boolean canUpgradeLevel(User user) {
//        Level currentLevel = user.getLevel();
//        switch (currentLevel) {
//            case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
//            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
//            case GOLD: return false;
//            default :
//                throw new IllegalArgumentException("Unknown LEVEL: " + currentLevel);
//        }
//    }

//    public void upgradeLevel(User user, UserDao userDao) {
//        user.upgradeLevel();
//        userDao.update(user);
//    }

//}

