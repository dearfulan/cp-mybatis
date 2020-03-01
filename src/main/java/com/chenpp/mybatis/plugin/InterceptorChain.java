package com.chenpp.mybatis.plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * 2020/2/29
 * created by chenpp
 */
public class InterceptorChain {

    //保存拦截器结婚
    private final List<Interceptor> interceptors = new ArrayList();

    public InterceptorChain() {
    }

    public Object pluginAll(Object target) throws Exception {
        for( int i = 0 ; i < interceptors.size() ; i++ ){
            //遍历所有的插件，使用每个插件对其做增强
            target = interceptors.get(i).plugin(target);
        }
        return target;
    }

    public void addInterceptor(Interceptor interceptor) {
        this.interceptors.add(interceptor);
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }
}
