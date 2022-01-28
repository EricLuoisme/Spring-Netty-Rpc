package com.rpc.example.spring.reference;

import com.rpc.example.IRegistryService;
import com.rpc.example.RegistryFactory;
import com.rpc.example.RegistryType;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class SpringRpcReferenceBean implements FactoryBean<Object> {

    private Object obj;
    private String serviceAddress;
    private int servicePort;
    private Class<?> interfaceClass;

    private String registryAddress;
    private byte registryType;

    /**
     * 在 SpringRpcReferencePostProcessor 中通过反射设置为init方法
     */
    public void init() {
        IRegistryService registryService = RegistryFactory.createRegistryService(
                this.registryAddress, RegistryType.findByCode(this.registryType));
        this.obj = Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new RpcInvokerProxy(registryService));
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

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public void setRegistryType(byte registryType) {
        this.registryType = registryType;
    }
}
