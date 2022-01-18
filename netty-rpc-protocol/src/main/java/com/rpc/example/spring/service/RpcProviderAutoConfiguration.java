package com.rpc.example.spring.service;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.UnknownHostException;

/**
 * 由Server端扫描该路径下Bean触发
 * 其中EnableConfigurationProperties触发扫描Server端的配置文件获取开放端口
 */
@Configuration
@EnableConfigurationProperties(RpcServerProperties.class)
public class RpcProviderAutoConfiguration {

    @Bean
    public SpringRpcProviderBean springRpcProviderBean(RpcServerProperties properties) throws UnknownHostException {
        return new SpringRpcProviderBean(properties.getServicePort());
    }
}
