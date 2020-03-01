package com.chenpp.mybatis.session;


/**
 * 2020/2/29
 * created by chenpp
 * SqlSession会话工厂，创建SqlSession
 */
public class SqlSessionFactory {

    private  Configuration configuration;

    /**
     * build方法用于初始化Configuration，解析配置文件的工作在Configuration的构造函数中
     * @return
     */
    public SqlSessionFactory build() throws ClassNotFoundException {
        configuration = new Configuration();
        return this;
    }


    public SqlSession openSession() throws Exception {
         return new DefaultSqlSession(configuration);
    }
}
