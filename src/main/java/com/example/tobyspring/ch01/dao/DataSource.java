package com.example.tobyspring.ch01.dao;

import org.springframework.context.annotation.Bean;

import javax.sql.CommonDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Wrapper;

public interface DataSource extends CommonDataSource, Wrapper {

    Connection getConnection() throws SQLException;
}
