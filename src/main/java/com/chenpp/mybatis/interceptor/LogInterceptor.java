package com.chenpp.mybatis.interceptor;

import com.chenpp.mybatis.executor.Executor;
import com.chenpp.mybatis.mapping.MappedStatement;
import com.chenpp.mybatis.plugin.*;



/**
 * 2020/2/29
 * created by chenpp
 * 自定义的插件 打印查询的SQL和时间
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class})
,@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class}) })
public class LogInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Long start = System.currentTimeMillis();
        Object[] args = invocation.getArgs() ;
        MappedStatement ms = (MappedStatement) args[0];
        String param = "";
        if(args[1] instanceof  Object[]){
            Object[] params = (Object[]) args[1];
            for(Object para : params){
                param += para + ",";
            }
        }
        System.out.println("执行的SQL:"+ms.getSql() + ",参数:"+param);
        try{
            Object result = invocation.proceed();
            return result;
        }finally {
            Long end = System.currentTimeMillis();
            System.out.println( "SQL执行时间: " + (end-start)+"ms");
        }
    }

    @Override
    public Object plugin(Object target) throws Exception {
        return Plugin.wrap(target,this);
    }
}
