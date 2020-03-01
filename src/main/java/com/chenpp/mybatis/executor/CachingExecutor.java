package com.chenpp.mybatis.executor;

import com.chenpp.mybatis.cache.CacheKey;
import com.chenpp.mybatis.mapping.MappedStatement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2020/2/29
 * created by chenpp
 * 带缓存的executor 这里直接将缓存的查询结果保存在Map里
 */
public class CachingExecutor implements  Executor {

    private  Executor delegate;

    public CachingExecutor(Executor delegate){
        this.delegate = delegate;
    }

    private static  Map<Integer, Object> cache = new HashMap<Integer, Object>();

    public <T> List<T> query(MappedStatement ms,  Object parameter) throws  Exception {
        CacheKey key = new CacheKey();
        key.update(ms.getSql());
        key.update(ms.getId());
        key.update(transferObjectToStr(parameter));
        if(cache.containsKey(key.hashCode())){
            System.out.println("SQL:"+ ms.getSql()+" 命中缓存 ");
            return (List) cache.get(key.hashCode());
        }
        List<T> result= delegate.query(ms,parameter);
        cache.put(key.hashCode(),result);
        return result;
    }

    public int update(MappedStatement ms,  Object parameter) throws Exception {
        //清除缓存，然后更新
        cache.clear();
        return delegate.update(ms,parameter);
    }

    //把Object[]转换成逗号拼接的字符串，因为对象的HashCode都不一样
    public String transferObjectToStr(Object obj){
        Object[] objs = null;
        if(obj == null){
            return "";
        }else if( obj instanceof Object[]){
            objs = (Object[]) obj;
        }else{
            objs = new Object[]{obj};
        }
        StringBuffer sb = new StringBuffer();
        if(objs !=null && objs.length > 0){
            for (Object objStr : objs) {
                sb.append(objStr.toString() + ",");
            }
        }
        int len = sb.length();
        if(len >0){
            sb.deleteCharAt(len-1);
        }
        return  sb.toString();
    }
}
