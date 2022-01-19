package com.rpc.example;

import com.rpc.example.zookeeper.ZookeeperRegistryService;

/**
 * 注册工厂, 根据注册地址 + 注册的集群, 获取注册中心配置
 */
public class RegistryFactory {

    public static IRegistryService createRegistryService(String address, RegistryType registryType) {
        IRegistryService registryService = null;
        try {
            switch (registryType) {
                case ZOOKEEPER:
                    registryService = new ZookeeperRegistryService(address);
                    break;
                case EUREKA:
                    // TODO
                    break;
                default:
                    registryService = new ZookeeperRegistryService(address);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registryService;
    }
}
