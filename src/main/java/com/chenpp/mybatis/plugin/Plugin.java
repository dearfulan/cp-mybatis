package com.chenpp.mybatis.plugin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 2020/2/29
 * created by chenpp
 */
public class Plugin implements InvocationHandler {

    private Object target;
    private Interceptor interceptor;
    private Map<Class<?>, Set<Method>> signatureMap;//保存拦截的类和方法的映射关系

    public Plugin(Object target,Interceptor interceptor, Map<Class<?>, Set<Method>> signatureMap){
        this.target = target;
        this.interceptor = interceptor;
        this.signatureMap = signatureMap;
    }

    public static Object wrap(Object target, Interceptor interceptor) throws Exception {
        Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(interceptor);
        Class<?> type = target.getClass();
        Class<?>[] interfaces = getAllInterfaces(type,signatureMap);
        return  interfaces.length > 0 ? Proxy.newProxyInstance(type.getClassLoader(), interfaces, new Plugin(target, interceptor,signatureMap)) : target;
    }

    /**
     *
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            Set<Method> methods = (Set)this.signatureMap.get(method.getDeclaringClass());
            return methods != null && methods.contains(method) ? this.interceptor.intercept(new Invocation(this.target, method, args)) : method.invoke(this.target, args);
        } catch (Exception e) {
            throw new Exception("interceptor invoke error",e);
        }
    }

    private static Class<?>[] getAllInterfaces(Class<?> type, Map<Class<?>, Set<Method>> signatureMap) {
        Set allInterfaces = new HashSet();
        for(; type != null; type = type.getSuperclass()) {
            Class[] typeInterfaces = type.getInterfaces();
            for(int i = 0; i < typeInterfaces.length; i++) {
                Class<?> interfaceClass = typeInterfaces[i];
                if (signatureMap.containsKey(interfaceClass)) {
                    allInterfaces.add(interfaceClass);
                }
            }
        }

        return (Class[])allInterfaces.toArray(new Class[allInterfaces.size()]);
    }

    //根据插件Interceptor上的注解，获取拦截的Class和对应的Method集合的映射关系
    private static Map<Class<?>, Set<Method>> getSignatureMap(Interceptor interceptor) throws Exception {
        Intercepts interceptsAnnotation = (Intercepts)interceptor.getClass().getAnnotation(Intercepts.class);
        if (interceptsAnnotation == null) {
            throw new Exception("No @Intercepts annotation was found in interceptor ");
        } else {
            Signature[] sigs = interceptsAnnotation.value();
            Map<Class<?>, Set<Method>> signatureMap = new HashMap();
            Signature[] var4 = sigs;

            for(int i = 0; i < sigs.length; i++) {
                Signature sig = sigs[i];
                Set<Method> methods = signatureMap.get(sig.type());
                if(methods == null ){
                    methods = new HashSet<Method>();
                    signatureMap.put(sig.type(),methods);
                }
                try {
                    Method method = sig.type().getMethod(sig.method(), sig.args());
                    methods.add(method);
                } catch (NoSuchMethodException e) {
                    throw new Exception("Could not find method on " + sig.type() + " named " + sig.method() + ". Cause: " + e, e);
                }
            }

            return signatureMap;
        }
    }

}
