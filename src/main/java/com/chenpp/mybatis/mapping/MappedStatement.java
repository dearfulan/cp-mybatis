package com.chenpp.mybatis.mapping;

import com.chenpp.mybatis.session.Configuration;

/**
 * 2020/2/29
 * created by chenpp
 */
public class MappedStatement {

    private Configuration configuration;
    private String commendType;//对应的命令类型 select,delete,insert,update
    private String id;//sql的statement id
    private String  sql;//对应的sql
    private Class<?> clazz;//sql 映射的实体类

    public MappedStatement(Configuration configuration, String id, String sql, String className,String commendType) throws ClassNotFoundException {
        this.configuration = configuration;
        this.id = id;
        this.sql = sql;
        this.clazz = Class.forName(className);
        this.commendType = commendType;
    }

    public MappedStatement(Configuration configuration, String id, String sql, Class<?> clazz,String commendType) {
        this.configuration = configuration;
        this.id = id;
        this.sql = sql;
        this.clazz = clazz;
        this.commendType = commendType;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getCommendType() {
        return commendType;
    }

    public void setCommendType(String commendType) {
        this.commendType = commendType;
    }
}
