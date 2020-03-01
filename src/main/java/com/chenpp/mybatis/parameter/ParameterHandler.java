package com.chenpp.mybatis.parameter;


import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 2020/2/29
 * created by chenpp
 */
public class ParameterHandler {

    public void setParameters(PreparedStatement psmt,Object[] parameters) throws SQLException {
        if( parameters != null && parameters.length >0 ){
            for( int i= 0 ; i < parameters.length ; i++){
                int k =i+1;
                if (parameters[i] instanceof Integer) {
                    psmt.setInt(k, (Integer) parameters[i]);
                } else if (parameters[i] instanceof Long) {
                    psmt.setLong(k, (Long) parameters[i]);
                } else if (parameters[i] instanceof String) {
                    psmt.setString(k , String.valueOf(parameters[i]));
                } else if (parameters[i] instanceof Boolean) {
                    psmt.setBoolean(k, (Boolean) parameters[i]);
                } else {
                    psmt.setString(k, String.valueOf(parameters[i]));
                }
            }
        }
    }
}
