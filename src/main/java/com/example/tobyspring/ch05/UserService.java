package com.example.tobyspring.ch05;

import com.example.tobyspring.ch01.dao.UserDao;
import com.example.tobyspring.ch01.domain.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import javax.sql.DataSource;
import java.util.List;


public class UserService{
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    UserDao userDao;

    private DataSource dataSource;
    private MailSender mailSender;
    private PlatformTransactionManager transactionManager;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
    //    private UserLevelUpgradePolicy userLevelUpgradePolicy;
//
//    public UserService(UserLevelUpgradePolicy userLevelUpgradePolicy) {
//        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
//    }
//
//    public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
//        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
//    }

    public UserService() {
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }


    public void upgradeLevels() {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            this.transactionManager.commit(status);
        } catch (Exception e){
            this.transactionManager.rollback(status);
            throw e;
        }
    }

    public void add(User user){
        if(user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }
    public boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            default :
                throw new IllegalArgumentException("Unknown LEVEL: " + currentLevel);
        }
    }
    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeEmail(user);
    }

    private void sendUpgradeEmail(User user){

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("useradmin@ksug.org");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이 " + user.getLevel().name());

        mailSender.send(mailMessage);


    }
}

