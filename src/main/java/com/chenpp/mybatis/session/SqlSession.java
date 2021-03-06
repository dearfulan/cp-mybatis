package com.chenpp.mybatis.session;


import java.util.List;

/**
 * 2020/2/29
 * created by chenpp
 * 对外提供的操作API
 */
public interface SqlSession {

    <T> T selectOne(String var1, Object var2) throws Exception;

    <T> List<T> selectList(String var1, Object var2) throws Exception;

    /**
     * 暂时只支持Object[]
     * */
    int delete(String var1, Object var2) throws Exception;

    int update(String var1, Object var2) throws Exception;

    int insert(String var1, Object var2) throws Exception;

    <T> T getMapper(Class<T> var1) throws Exception;

    Configuration getConfiguration();
}
