package com.example.tobyspring.ch01.dao;

import com.example.tobyspring.ch01.domain.User;
import com.example.tobyspring.ch03.dao.AddStatement;
import com.example.tobyspring.ch03.dao.DeleteAllStatement;
import com.example.tobyspring.ch03.dao.JdbcContext;
import com.example.tobyspring.ch03.dao.StatementStrategy;
import com.example.tobyspring.ch04.DuplicateUserIdException;
import com.mysql.cj.exceptions.MysqlErrorNumbers;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public interface UserDao {

    void add(User user);

    User get(String id);

    List<User> getAll();

    void deleteAll();

    int getCount();

    void update(User user1);


}

