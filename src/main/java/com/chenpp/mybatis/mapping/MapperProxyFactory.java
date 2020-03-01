package com.chenpp.mybatis.mapping;

import com.chenpp.mybatis.session.SqlSession;

import java.lang.reflect.Proxy;

/**
 * 2020/2/29
 * created by chenpp
 */
public class MapperProxyFactory<T> {

    //需要代理的Mapper接口
    private Class mapperInterface;

    public MapperProxyFactory(Class clazz){
        this.mapperInterface = clazz;
    }

    public  <T> T  newInstance(SqlSession sqlSession){
        MapperProxy<T> mapperProxy = new MapperProxy<T>(sqlSession,mapperInterface);
        return (T) Proxy.newProxyInstance(this.mapperInterface.getClassLoader(), new Class[]{this.mapperInterface}, mapperProxy);
    }

}
