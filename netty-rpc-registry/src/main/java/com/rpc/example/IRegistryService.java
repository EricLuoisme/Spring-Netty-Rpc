package com.rpc.example;

public interface IRegistryService {

    /**
     * 服务注册
     */
    void register(ServiceInfo serviceInfo);

    /**
     * 服务发现
     */
    ServiceInfo discovery(String serviceName);

}
