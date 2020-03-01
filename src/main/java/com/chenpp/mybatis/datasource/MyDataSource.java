package com.chenpp.mybatis.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 2020/2/29
 * created by chenpp
 */
public class MyDataSource {

    private String driverName;
    private String url;
    private String username;
    private String password;

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        Connection connection = DriverManager.getConnection(url,username,password);
        return connection;
    }

    public void release(Connection connection) throws  SQLException {
       if(connection != null){
           connection.close();
       }
    }
}
