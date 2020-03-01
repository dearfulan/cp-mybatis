package com.chenpp.mybatis.session;

import com.chenpp.mybatis.executor.Executor;
import com.chenpp.mybatis.mapping.MappedStatement;

import java.sql.SQLException;
import java.util.List;

/**
 * 2020/2/29
 * created by chenpp
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    private Executor executor ;

    public DefaultSqlSession(Configuration configuration ) throws Exception {
        this.configuration = configuration;
        // 根据全局配置决定是否使用缓存装饰
        this.executor = configuration.newExecutor();;
    }

    public <T> T selectOne(String statement, Object parameter) throws Exception {
        List<T> list = this.selectList(statement, parameter);
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() > 1) {
            throw new Exception("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    public <E> List<E> selectList(String statement, Object parameter) throws Exception {
        List list;
        try {
            MappedStatement ms = this.configuration.getMappedStatement(statement);
            list = this.executor.query(ms, parameter);
        } catch (Exception e) {
            throw new Exception("Error querying database",e);
        }

        return list;
    }

    public int delete(String var1) throws Exception {
        MappedStatement ms = this.configuration.getMappedStatement(var1);
        return this.executor.update(ms,new Object[]{});
    }

    public int delete(String var1, Object var2) throws Exception {
        MappedStatement ms = this.configuration.getMappedStatement(var1);
        return this.executor.update(ms,var2);
    }

    public int update(String var1) throws Exception {
        MappedStatement ms = this.configuration.getMappedStatement(var1);
        return this.executor.update(ms,new Object[]{});
    }

    public int update(String var1, Object var2) throws Exception {
        MappedStatement ms = this.configuration.getMappedStatement(var1);
        return this.executor.update(ms,var2);
    }

    public int insert(String var1) throws Exception {
        MappedStatement ms = this.configuration.getMappedStatement(var1);
        return this.executor.update(ms,new Object[]{});
    }

    public int insert(String var1, Object var2) throws Exception {
        MappedStatement ms = this.configuration.getMappedStatement(var1);
        return this.executor.update(ms,var2);
    }

    public <T> T getMapper(Class<T> clazz) throws Exception {
        return this.configuration.getMapper(clazz,this);
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
