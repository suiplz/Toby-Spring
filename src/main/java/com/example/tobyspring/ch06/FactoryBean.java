package com.example.tobyspring.ch06;

public interface FactoryBean<T> {

    T getObject() throws Exception;

    Class<? extends T> getObjectType();

    boolean isSingleton();

}
