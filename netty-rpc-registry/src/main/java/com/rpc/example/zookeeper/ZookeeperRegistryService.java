package com.rpc.example.zookeeper;

import com.rpc.example.IRegistryService;
import com.rpc.example.ServiceInfo;
import com.rpc.example.loadBalance.ILoadBalance;
import com.rpc.example.loadBalance.RandomLoadBalance;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;
import java.util.List;

@Slf4j
public class ZookeeperRegistryService implements IRegistryService {

    private final static String REGISTRY_PATH = "/registry";
    // curator 封装的服务注册与发现
    private final ServiceDiscovery<ServiceInfo> serviceDiscovery;
    private ILoadBalance<ServiceInstance<ServiceInfo>> loadBalance;


    public ZookeeperRegistryService(String registryAddress) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(registryAddress,
                new ExponentialBackoffRetry(1000, 3));
        client.start();
        JsonInstanceSerializer<ServiceInfo> serializer = new JsonInstanceSerializer<>(ServiceInfo.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class)
                .client(client)
                .serializer(serializer)
                .basePath(REGISTRY_PATH)
                .build();
        this.serviceDiscovery.start();
        // 暂时固定为随机路由
        this.loadBalance = new RandomLoadBalance();
    }


    @Override
    public void register(ServiceInfo serviceInfo) {
        try {
            log.info("======== Begin registry ServiceInfo to Zookeeper-Server ======");
            ServiceInstance<ServiceInfo> serviceInstance = null;
            serviceInstance = ServiceInstance.<ServiceInfo>builder()
                    .name(serviceInfo.getServiceName())
                    .address(serviceInfo.getServiceAddress())
                    .port(serviceInfo.getServicePort())
                    .payload(serviceInfo)
                    .build();
            this.serviceDiscovery.registerService(serviceInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ServiceInfo discovery(String serviceName) {
        try {
            Collection<ServiceInstance<ServiceInfo>> serviceInstances = this.serviceDiscovery.queryForInstances(serviceName);
            // 动态路由
            ServiceInstance<ServiceInfo> select = this.loadBalance.select((List<ServiceInstance<ServiceInfo>>) serviceInstances);
            if (select != null) {
                return select.getPayload();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
