package com.example.tobyspring.ch07;

import javax.xml.bind.annotation.*;
import java.sql.SQLType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sqlmapType", propOrder = {"sql"})
@XmlRootElement(name = "sqlmap")
public class Sqlmap{

    @XmlElement(required = true)
    protected List<SqlType> sql;

    public List<SqlType> getSql() {
        if (sql == null) {
            sql = new ArrayList<SqlType>();
        }
        return this.sql;
    }
}
