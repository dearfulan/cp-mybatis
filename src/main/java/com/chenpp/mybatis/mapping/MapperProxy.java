package com.chenpp.mybatis.mapping;

import com.chenpp.mybatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 2020/2/29
 * created by chenpp
 * Mapper的增强类，实现InvocationHandler接口
 * 因为我们的代理对象在执行的时候可以执行对应的SQL,所以需要持有一个会话对象SqlSession
 */
public class MapperProxy<T> implements InvocationHandler {

    private SqlSession sqlSession;

    private Class<T> clazz;

    public MapperProxy(SqlSession sqlSession,Class clazz){
        this.sqlSession = sqlSession;
        this.clazz = clazz;
    }
    /**
     * 用来创建Mapper的代理对象
     *
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String statementId = clazz.getName()+"."+ method.getName();
        //如果是Object方法，执行原来的逻辑,否则执行代理方法
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(proxy, args);
        }
        // 如果根据接口类型+方法名能找到映射的SQL，则执行SQL
        MappedStatement ms = sqlSession.getConfiguration().getMappedStatement(statementId);
        if(ms == null){
            return method.invoke(proxy,args);
        }
        if("select".equals(ms.getCommendType())){
            //这里直接使用查询一个的方法
            Class<?> clazz = method.getReturnType();
            if(clazz == List.class){
                return sqlSession.selectList(statementId,args);
            }
            return sqlSession.selectOne(statementId,args);
        }else if("update".equals(ms.getCommendType())){
            return sqlSession.update(statementId,args);
        }else if("delete".equals(ms.getCommendType())){
            return sqlSession.delete(statementId,args);
        }else if("insert".equals(ms.getCommendType())){
            return sqlSession.insert(statementId,args);
        }
        // 否则直接执行被代理对象的原方法
        return method.invoke(proxy, args);
    }
}
