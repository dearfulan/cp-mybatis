package com.chenpp.mybatis.session;

import com.chenpp.mybatis.annoation.*;
import com.chenpp.mybatis.datasource.MyDataSource;
import com.chenpp.mybatis.executor.CachingExecutor;
import com.chenpp.mybatis.executor.Executor;
import com.chenpp.mybatis.executor.SimpleExecutor;
import com.chenpp.mybatis.plugin.Interceptor;
import com.chenpp.mybatis.plugin.InterceptorChain;
import com.chenpp.mybatis.mapping.MappedStatement;
import com.chenpp.mybatis.mapping.MapperRegistry;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * 2020/2/29
 * created by chenpp
 * 核心配置类
 */
public class Configuration {

    public static ResourceBundle sqlMappings; // 解析sql配置文件，这里简单处理成Properties
    public static ResourceBundle configProperties; // 解析Mybatis全局配置 简单处理成Properties
    public MyDataSource dataSource ;
    /**
     * 用于保存Mapper类和对应的MapperProxy工厂的关系
     * */
    public final MapperRegistry mapperRegistry = new MapperRegistry(this);

    /**
     * 维护接口方法与MappedStatement的关系(这里保存了和sql相关的各种参数)
     * */
    public Map<String, MappedStatement> mappedStatements = new HashMap<String, MappedStatement>();

    protected boolean cacheEnabled;//是否开启二级缓存

    private InterceptorChain interceptorChain = new InterceptorChain(); // 插件

    private List<Class<?>> mapperList = new ArrayList<Class<?>>(); // 所有Mapper接口

    static{
        sqlMappings = ResourceBundle.getBundle("sql");
        configProperties = ResourceBundle.getBundle("mybatis");
    }

    //解析配置文件
    public Configuration() throws ClassNotFoundException {
        parse();
    }

    private void parse() throws ClassNotFoundException {
        //解析数据源信息
        parseDataSource();
        //解析全局配置文件
        cacheEnabled = "true".equalsIgnoreCase(configProperties.getString("cache.enabled")) ? true : false;
        //解析plugin插件，添加到拦截器链
        parsePlugins();
        //解析Mapper文件
        parseMappers();

    }

    private void parsePlugins() {
        for(String key : configProperties.keySet()) {
            if(key.startsWith("plugin.path")){
                String pluginClassName = configProperties.getString(key);
                Interceptor interceptor = null;
                try {
                    interceptor = (Interceptor) Class.forName(pluginClassName).newInstance();
                    interceptorChain.addInterceptor(interceptor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public MyDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(MyDataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void parseDataSource() {
        dataSource = new MyDataSource();
        String driverName = configProperties.getString("jdbc.driver");
        String url = configProperties.getString("jdbc.url");
        String username = configProperties.getString("jdbc.username");
        String password = configProperties.getString("jdbc.password");
        dataSource.setDriverName(driverName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
    }

    private void parseMappers() throws ClassNotFoundException {
        String mapperScan = configProperties.getString("mapper.scan.path");
        doScan(mapperScan);
        //1.如果sql.properties里有配置sql的映射关系，根据这个注册Mapper信息和statement id 和MappedStatement的映射关系
        for(String key : sqlMappings.keySet()){
            String statementId = key;
            String value = sqlMappings.getString(statementId);
            //这里简单通过--把sql和映射的实体类映射上(--在MySQL表示注释,所以SQL里不会出现)
            String[] values = value.split("--");
            String sql = values[0];
            String className = values[1].trim();
            String commendType = values[2].trim();
            Class<?> clazz = Class.forName(className);
            MappedStatement mappedStatement = new MappedStatement(this,statementId,sql,clazz,commendType);
            mappedStatements.put(statementId,mappedStatement);
            mapperRegistry.addMapper(clazz);
        }
        //2.根据Mapper的类上的注解获取对应的映射关系
        for(Class mapperClass : mapperList) {
            if (mapperClass.isAnnotationPresent(Entity.class)) {
                Entity entity = (Entity) mapperClass.getAnnotation(Entity.class);
                //这里把Entity注解上的value直接写上对应映射的实体class(相当于MyBatis里ResultType的作用)
                Class clazz = entity.value();
                mapperRegistry.addMapper(mapperClass);
                //3.解析方法上的注解
                Method[] methods = mapperClass.getMethods();
                for (Method method : methods) {
                    // 解析@Select注解的SQL语句
                    if (method.isAnnotationPresent(Select.class)) {
                        Select select = (Select) method.getAnnotation(Select.class);
                        addMappedStatement(method, select.value(), clazz,"select");
                    } else if (method.isAnnotationPresent(Delete.class)) {
                        Delete delete = (Delete) method.getAnnotation(Delete.class);
                        addMappedStatement(method, delete.value(), clazz,"delete");
                    } else if (method.isAnnotationPresent(Update.class)) {
                        Update update = (Update) method.getAnnotation(Update.class);
                        addMappedStatement(method, update.value(), clazz,"update");
                    } else if (method.isAnnotationPresent(Insert.class)) {
                        Insert insert = (Insert) method.getAnnotation(Insert.class);
                        addMappedStatement(method, insert.value(), clazz,"insert");
                    }
                }
            }
        }

    }

    private void addMappedStatement(Method method,String sql,Class<?> clazz ,String commendType){
        String statementId = method.getDeclaringClass().getName() + "." +method.getName();
        MappedStatement mappedStatement = new MappedStatement(this,statementId,sql,clazz,commendType);
        mappedStatements.put(statementId, mappedStatement);
    }

    private void doScan(String mapperScanPath) throws ClassNotFoundException {
        URL url = this.getClass().getResource("/" + mapperScanPath.replaceAll("\\.","/"));

        for (File file :  new File(url.getPath()).listFiles()) {
            if(file.isDirectory()){
                doScan(mapperScanPath +"/" + file.getPath());
            }else{
                if(!file.getName().endsWith(".class")){ continue;}
                String className = (mapperScanPath + "." + file.getName().replace(".class",""));
                mapperList.add(Class.forName(className));
            }
        }
    }


    public MappedStatement getMappedStatement(String statementId) {
        return mappedStatements.get(statementId);
    }

    public Executor newExecutor() throws Exception {
        Executor executor = null;

        if(cacheEnabled){
            executor = new CachingExecutor(new SimpleExecutor());
        }else{
            executor = new SimpleExecutor();
        }
        //使用插件拦截Executor,对Executor进行代理最后返回最后的代理对象
        executor = (Executor)this.interceptorChain.pluginAll(executor);

        return executor;
    }

    public <T> T getMapper(Class<T> clazz, SqlSession sqlSession) throws Exception {
        return this.mapperRegistry.getMapper(clazz,sqlSession);
    }
}
