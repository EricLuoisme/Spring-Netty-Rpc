package com.rpc.example;

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
        SpringApplication.run(NettyRpcProducer.class, args);
    }
}
