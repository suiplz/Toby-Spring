package com.example.tobyspring.ch07;

public class SQLRetrievalFailureException extends RuntimeException {

    public SQLRetrievalFailureException(String message) {
        super(message);
    }

    public SQLRetrievalFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
