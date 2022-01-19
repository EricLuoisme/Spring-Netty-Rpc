package com.rpc.example.loadBalance;

import com.rpc.example.ServiceInfo;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;

public abstract class AbstractLoadBalance implements ILoadBalance<ServiceInstance<ServiceInfo>> {
    @Override
    public ServiceInstance<ServiceInfo> select(List<ServiceInstance<ServiceInfo>> services) {
        if (null == services || services.size() == 0) {
            return null;
        }
        if (services.size() == 1) {
            return services.get(0);
        }
        return doSelect(services);
    }

    protected abstract ServiceInstance<ServiceInfo> doSelect(List<ServiceInstance<ServiceInfo>> services);
}
