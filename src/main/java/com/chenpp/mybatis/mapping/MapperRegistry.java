package com.chenpp.mybatis.mapping;


import com.chenpp.mybatis.session.Configuration;
import com.chenpp.mybatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

/**
 * 2020/2/29
 * created by chenpp
 * 注册Mapper信息的类
 */
public class MapperRegistry {

    private final Configuration config;
    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap();

    public MapperRegistry(Configuration config) {
        this.config = config;
    }

    public <T> T  getMapper(Class<T> clazz, SqlSession sqlSession) throws Exception {
        MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) this.knownMappers.get(clazz);
        if (mapperProxyFactory == null) {
            throw new Exception("Type " + clazz + " is not known to the MapperRegistry.");
        } else {
            try {
                //通过Mapper代理工厂创建代理对象
                return  mapperProxyFactory.newInstance(sqlSession);
            } catch (Exception var5) {
                throw new Exception("Error getting mapper instance. ");
            }
        }
    }

    public void addMapper(Class<?> clazz) {
        knownMappers.put(clazz,new MapperProxyFactory(clazz));
    }
}
