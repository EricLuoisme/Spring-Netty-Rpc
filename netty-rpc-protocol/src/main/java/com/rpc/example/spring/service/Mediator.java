package com.rpc.example.spring.service;

import com.rpc.example.core.RpcRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 确保为单例
 */
public class Mediator {
    public static Map<String, BeanMethod> beanMethodMap = new ConcurrentHashMap<>();

    private volatile static Mediator instance = null;

    private Mediator() {

    }

    /**
     * 双重检查实现单例
     */
    public static Mediator getInstance() {
        if (null == instance) {
            synchronized (Mediator.class) {
                if (null == instance) {
                    instance = new Mediator();
                }
            }
        }
        return instance;
    }

    /**
     * 带有注解的Bean及其Method被绑定到Map, 并在此时被Rpc请求调用时, 反射调用请求
     * 在Channel的Handler接受到RpcRequest时, 可以直接调用这里, 来获得执行结果
     */
    public Object processor(RpcRequest request) {
        String key = request.getClassName() + "." + request.getMethodName();
        BeanMethod bm = beanMethodMap.get(key);
        if (null == bm) {
            return null;
        }

        Object bean = bm.getBean();
        Method method = bm.getMethod();

        try {
            return method.invoke(bean, request.getParams());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
}
