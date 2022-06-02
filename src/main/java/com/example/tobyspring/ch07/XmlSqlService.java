package com.example.tobyspring.ch07;

import com.example.tobyspring.ch01.dao.UserDao;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.sql.SQLType;
import java.util.HashMap;
import java.util.Map;

public class XmlSqlService implements SqlService{
    private Map<String, String> sqlMap = new HashMap<String, String>();

    private String sqlmapFile;

    public XmlSqlService() {
        }

    @PostConstruct
    public void loadSql() {
        String contextPath = Sqlmap.class.getPackage().getName();
        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream is = UserDao.class.getResourceAsStream(this.sqlmapFile);
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);

            for (SqlType sql : sqlmap.getSql()) {
                sqlMap.put(sql.getKey(), sql.getValue());
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }


        public String getSql(String key) throws SQLRetrievalFailureException {
        String sql = sqlMap.get(key);
        if (sql == null)
            throw new SQLRetrievalFailureException(key + "를 이용해서 SQL을 찾을 수 없습니다.");
        else return sql;
    }

    public void setSqlmapFile(String sqlmapFile) {
        this.sqlmapFile = sqlmapFile;
    }
}

