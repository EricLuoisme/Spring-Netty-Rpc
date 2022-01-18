package com.rpc.example;

import com.rpc.example.protocol.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 扫描如下包, 将其存入spring容器
 */
@ComponentScan(basePackages = {"com.rpc.example.spring", "com.rpc.example.service"})
@SpringBootApplication
public class NettyRpcProducer {
    public static void main(String[] args) {
        // 启动Spring容器
        SpringApplication.run(NettyRpcProducer.class, args);
        // 启动自定义的Netty
        new NettyServer("127.0.0.1", 8080).startNettyServer();
    }
}
