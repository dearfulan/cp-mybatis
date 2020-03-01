package com.chenpp.mybatis.plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 2020/2/29
 * created by chenpp
 * 保存代理类的信息，目标对象，执行方法和参数
 */
public class Invocation {

    private Object target ;

    private Method methdod ;

    private Object[] args;

    public Invocation(Object target,Method method,Object[] args){
        this.target = target;
        this.methdod = method;
        this.args = args;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Method getMethdod() {
        return methdod;
    }

    public void setMethdod(Method methdod) {
        this.methdod = methdod;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object proceed() throws InvocationTargetException, IllegalAccessException {
       return  methdod.invoke(target,args);
    }
}
