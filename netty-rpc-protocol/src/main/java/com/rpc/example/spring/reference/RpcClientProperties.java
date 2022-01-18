package com.rpc.example.spring.reference;

import lombok.Data;

@Data
public class RpcClientProperties {
    private String serviceAddress = "172.29.128.1";
    private int servicePort = 8888;
}
