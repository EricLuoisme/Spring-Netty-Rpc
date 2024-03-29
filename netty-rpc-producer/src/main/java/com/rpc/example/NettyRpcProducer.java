package com.rpc.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 扫描如下包, 将其存入spring容器
 * 其中Netty自启动, 是通过扫描com.rpc.example.spring.service包, 然后里面有Bean注解, 从而调用的Netty-Server启动
 */
@ComponentScan(basePackages = {
        "com.rpc.example.spring.service", // for spring bean
        "com.rpc.example.service"}) // for local @RpcRemoteService annotation working
@SpringBootApplication
public class NettyRpcProducer {
    public static void main(String[] args) {
        // 启动Spring容器
        SpringApplication.run(NettyRpcProducer.class, args);
    }
}
