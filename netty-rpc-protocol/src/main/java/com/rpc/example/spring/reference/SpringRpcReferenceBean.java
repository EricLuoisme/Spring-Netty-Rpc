package com.rpc.example.spring.reference;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class SpringRpcReferenceBean implements FactoryBean<Object> {

    private Object obj;
    private String serviceAddress;
    private int servicePort;
    private Class<?> interfaceClass;


    public void init() {
        this.obj = Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new RpcInvokerProxy(serviceAddress, servicePort));
    }


    @Override
    public Object getObject() throws Exception {
        return obj;
    }

    @Override
    public Class<?> getObjectType() {
        return this.interfaceClass;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }
}
