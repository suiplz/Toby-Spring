package com.example.tobyspring.ch07;

public interface SqlService {
    String getSql(String key) throws SQLRetrievalFailureException;
}
