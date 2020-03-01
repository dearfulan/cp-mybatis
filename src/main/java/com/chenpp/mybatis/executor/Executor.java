package com.chenpp.mybatis.executor;

import com.chenpp.mybatis.mapping.MappedStatement;

import java.sql.SQLException;
import java.util.List;

/**
 * 2020/2/29
 * created by chenpp
 */
public interface Executor {

    <T> List<T> query(MappedStatement ms, Object parameter) throws Exception;

    int update(MappedStatement ms, Object parameter) throws Exception;

}
