package com.example.tobyspring.ch03;

public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}
