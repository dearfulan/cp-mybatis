package com.chenpp.mybatis.executor;

import com.chenpp.mybatis.mapping.MappedStatement;
import com.chenpp.mybatis.result.ResultHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * 2020/2/29
 * created by chenpp
 */
public class SimpleExecutor implements  Executor {

    //执行实际的JDBC查询操作
    public <T> List<T>  query(MappedStatement ms, Object parameter) throws Exception{
        //创建一个StatementHandler
        StatementHandler statementHandler = new StatementHandler();
        return statementHandler.query(ms, parameter);
    }


    public int update(MappedStatement ms, Object parameter) throws Exception {
        //创建一个StatementHandler
        StatementHandler statementHandler = new StatementHandler();
        return statementHandler.update(ms, parameter);
    }
}
