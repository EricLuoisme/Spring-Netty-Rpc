package com.rpc.example.spring.reference;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

@Configuration
public class RpcReferenceAutoConfiguration implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public SpringRpcReferencePostProcessor postProcessor() {
        RpcClientProperties rc = new RpcClientProperties();
//        rc.setServiceAddress(this.environment.getProperty("client.rpc.serviceAddress"));
//        rc.setServicePort(Integer.parseInt(Objects.requireNonNull(this.environment.getProperty("client.rpc.servicePort"))));
        rc.setRegistryAddress(this.environment.getProperty("client.rpc.registryAddress"));
        rc.setRegistryType(Byte.parseByte(Objects.requireNonNull(this.environment.getProperty("client.rpc.registryType"))));
        return new SpringRpcReferencePostProcessor(rc);
    }
}
