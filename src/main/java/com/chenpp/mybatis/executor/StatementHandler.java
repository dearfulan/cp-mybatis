package com.chenpp.mybatis.executor;

import com.chenpp.mybatis.mapping.MappedStatement;
import com.chenpp.mybatis.parameter.ParameterHandler;
import com.chenpp.mybatis.result.ResultHandler;
import com.chenpp.mybatis.session.Configuration;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 2020/2/29
 * created by chenpp
 * 用于调用JDBC API，执行SQL
 * 这里简化了很多操作，直接创建了ResultHandler和ParameterHandler来处理参数和结果集
 */
public class StatementHandler {

    private ResultHandler resultSetHandler = new ResultHandler();


    public <T> List<T> query(MappedStatement ms, Object parameter) throws SQLException, ClassNotFoundException {
        Configuration configuration = ms.getConfiguration();
        Connection conn = configuration.getDataSource().getConnection();
        PreparedStatement ps = conn.prepareStatement(ms.getSql());
        ParameterHandler parameterHandler = new ParameterHandler();
        Object[] params = null;
        //这里参数的处理做简化，暂时只处理Object和Object[]数组
        if( parameter == null){
            params = null;
        }else if(parameter instanceof  Object[]){
            params = (Object[]) parameter;
        }else{
            params = new Object[]{parameter};
        }
        parameterHandler.setParameters(ps,params);
        List<T> list = null;
        try {
            ps.execute();
            list = resultSetHandler.handleResult(ps.getResultSet(), ms.getClazz());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            configuration.getDataSource().release(conn);
        }

        return list;
    }

    public int update(MappedStatement ms, Object parameter) throws Exception {
        Configuration configuration = ms.getConfiguration();
        Connection conn = configuration.getDataSource().getConnection();
        PreparedStatement ps = conn.prepareStatement(ms.getSql());
        ParameterHandler parameterHandler = new ParameterHandler();
        Object[] params = null;
        //这里参数的处理做简化，暂时只处理Object和Object[]数组和实体类(不考虑Map等复杂类型)
        if( parameter == null){
            params = null;
        }else if(parameter instanceof  Object[]){
            params = (Object[]) parameter;
        }else if(parameter.getClass() == ms.getClazz()){
            params = parseEntityToObject(parameter);
        }else {
            params = new Object[]{parameter};
        }
        boolean result = false;
        try{
            parameterHandler.setParameters(ps,params);
            result = ps.execute();
        }finally {
            configuration.getDataSource().release(conn);
        }
        return result ? 1 : 0;
    }

    /**
     * 根据实体类对象映射成对应的Object[]
     * 简单实现，暂时不考虑顺序或者insert参数不是全部的情形
     * TODO 对于顺序无法处理，还是需要通过SQL知道每一个占位符的列名是什么，根据映射关系匹配到对应的实体类字段名
     * 最后通过反射获取
     * */
    public Object[] parseEntityToObject(Object parameter) throws Exception {
        Method[] methods = parameter.getClass().getDeclaredMethods();
        List<Object> values = new ArrayList<Object>();
        for(Method method : methods){
            if(method.getName().startsWith("get")){
               //拿到对应的属性值
               Object value = method.invoke(parameter,new Object[]{});
               values.add(value);
            }
        }
        return values.toArray();
    }
}
