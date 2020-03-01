package com.chenpp.mybatis.plugin;

/**
 * 2020/2/29
 * created by chenpp
 * 插件需要实现的接口类
 */
public interface Interceptor {

    Object intercept(Invocation var1) throws Throwable;

    public Object plugin(Object target) throws Exception;
}
