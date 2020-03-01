package com.chenpp.mybatis.result;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 2020/2/29
 * created by chenpp
 * 处理结果集的Handler
 */
public class ResultHandler {

    public <T> List<T> handleResult(ResultSet resultSet, Class type)  {
        List<T> list = new ArrayList<T>();

        try {
            // 遍历结果集
            while (resultSet.next()) {
                //通过反射创建实例
                T instance  = (T) type.newInstance();
                for (Field field : instance.getClass().getDeclaredFields()) {
                    setValue(instance, field, resultSet);
                }
                list.add(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private void setValue(Object pojo, Field field, ResultSet resultSet) {
        //获取实体类的set方法
        try{
            Method setMethod = pojo.getClass().getMethod("set" + upperToFirstChar(field.getName()), field.getType());
            setMethod.invoke(pojo, getResult(resultSet, field));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Object getResult(ResultSet rs, Field field) throws SQLException {
        Class type = field.getType();
        String columnName = humpToUnderline(field.getName());
        if (Integer.class == type ) {
            return rs.getInt(columnName);
        }else if (String.class == type) {
            return rs.getString(columnName);
        }else if(Long.class == type){
            return rs.getLong(columnName);
        }else if(Boolean.class == type){
            return rs.getBoolean(columnName);
        }else if(Double.class == type){
            return rs.getDouble(columnName);
        }else{
            return rs.getString(columnName);
        }
    }

    // 数据库下划线转Java驼峰命名
    public  String humpToUnderline(String para){
        StringBuilder sb= new StringBuilder(para);
        int temp=0;
        if (!para.contains("_")) {
            for(int i=0 ; i < para.length() ; i++){
                //对于每个大写字母，在其前面加上"_"
                if(Character.isUpperCase(para.charAt(i))){
                    sb.insert(i+temp, "_");
                    temp += 1;
                }
            }
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 单词首字母大写
     */
    private static String upperToFirstChar(String word){
        if(Character.isUpperCase(word.charAt(0))){
            return word;
        }
        return Character.toUpperCase(word.charAt(0))+word.substring(1);
    }
}
