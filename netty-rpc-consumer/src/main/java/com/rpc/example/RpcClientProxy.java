package com.rpc.example;

import java.lang.reflect.Proxy;

public class RpcClientProxy {
    public <T> T clientProxy(final Class<T> interfaceClazz, final String host, int port) {
        return (T) Proxy.newProxyInstance(interfaceClazz.getClassLoader(),
                new Class<?>[]{interfaceClazz},
                new RpcInvokerProxy(host, port));
    }
}
