package com.example.tobyspring.ch07;

import java.util.Map;

public class SimpleSqlService implements SqlService{

    private Map<String, String> sqlMap;

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }

    public String getSql(String key) throws SQLRetrievalFailureException {
        String sql = sqlMap.get(key);
        if(sql==null)
            throw new SQLRetrievalFailureException(key + "에 대한 SQL을 찾을 수 없습니다.");
        else
            return sql;
    }

}
