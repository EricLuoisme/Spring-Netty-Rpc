package com.rpc.example.spring.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "service.rpc")
public class RpcServerProperties {
    private int servicePort;
    private String registryAddress;
    private byte registryType;
}
