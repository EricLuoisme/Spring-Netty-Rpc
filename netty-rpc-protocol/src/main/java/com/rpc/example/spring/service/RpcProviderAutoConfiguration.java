package com.rpc.example.spring.service;

import com.rpc.example.IRegistryService;
import com.rpc.example.RegistryFactory;
import com.rpc.example.RegistryType;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.UnknownHostException;

/**
 * 由Server端扫描该路径下Bean触发(也可以在spring.factories里面将org.springframework....enableConfiguration=具体类路径, 进行触发扫描)
 * 其中EnableConfigurationProperties触发扫描Server端的配置文件获取开放端口
 */
@Configuration
@EnableConfigurationProperties(RpcServerProperties.class)
public class RpcProviderAutoConfiguration {

    @Bean
    public SpringRpcProviderBean springRpcProviderBean(RpcServerProperties properties) throws UnknownHostException {
//        return new SpringRpcProviderBean(properties.getServicePort());

        // 调用注册服务, 获取内容
        IRegistryService registryService = RegistryFactory.createRegistryService(properties.getRegistryAddress(),
                RegistryType.findByCode(properties.getRegistryType()));
        return new SpringRpcProviderBean(properties.getServicePort(), registryService);
    }
}
