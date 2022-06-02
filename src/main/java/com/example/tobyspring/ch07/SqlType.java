package com.example.tobyspring.ch07;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sqlType", propOrder = {"value"})
public class SqlType {

    @XmlValue
    protected String value;

    @XmlAttribute(required = false)
    protected String key;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
