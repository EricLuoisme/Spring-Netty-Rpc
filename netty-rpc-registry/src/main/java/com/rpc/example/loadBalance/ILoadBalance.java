package com.rpc.example.loadBalance;

import java.util.List;

public interface ILoadBalance<T> {
    // 路由
    T select(List<T> services);
}
