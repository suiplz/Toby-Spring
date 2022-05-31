package com.example.tobyspring.ch06;

public class MessageFactoryBean implements FactoryBean<Message>{

    String text;

    public void setText(String text) {
        this.text = text;
    }

    public Message getObject() throws Exception {
        return Message.newMessage(this.text);
    }

    public Class<? extends Message> getObjectType() {
        return Message.class;
    }

    public boolean isSingleton() {
        return false;
    }
}
