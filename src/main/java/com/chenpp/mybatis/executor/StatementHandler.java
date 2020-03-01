package com.chenpp.mybatis.executor;

import com.chenpp.mybatis.mapping.MappedStatement;
import com.chenpp.mybatis.parameter.ParameterHandler;
import com.chenpp.mybatis.result.ResultHandler;
import com.chenpp.mybatis.session.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * 2020/2/29
 * created by chenpp
 */
public class StatementHandler {

    private ResultHandler resultSetHandler = new ResultHandler();


    public <T> List<T> query(MappedStatement ms, Object parameter) throws SQLException, ClassNotFoundException {
        Configuration configuration = ms.getConfiguration();
        Connection conn = configuration.getDataSource().getConnection();
        PreparedStatement ps = conn.prepareStatement(ms.getSql());
        ParameterHandler parameterHandler = new ParameterHandler();
        Object[] params = null;
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

    public int update(MappedStatement ms, Object parameter) throws SQLException, ClassNotFoundException {
        Configuration configuration = ms.getConfiguration();
        Connection conn = configuration.getDataSource().getConnection();
        PreparedStatement ps = conn.prepareStatement(ms.getSql());
        ParameterHandler parameterHandler = new ParameterHandler();
        Object[] params = null;
        if( parameter == null){
            params = null;
        }else if(parameter instanceof  Object[]){
            params = (Object[]) parameter;
        }else if(parameter.getClass() == ms.getClazz()){
            params = new Object[]{parameter};
        }
        boolean result = false;
        try{
            parameterHandler.setParameters(ps,params);
            result = ps.execute();
        }finally {
            configuration.getDataSource().release(conn);
        }
         ps.execute();
        return result ? 1 : 0;
    }
}
