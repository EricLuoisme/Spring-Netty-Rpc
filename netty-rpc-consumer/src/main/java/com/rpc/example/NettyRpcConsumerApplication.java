package com.rpc.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.rpc.example.spring.reference",
        "com.rpc.example.annotation",
        "com.rpc.example.controller"})
@SpringBootApplication
public class NettyRpcConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(NettyRpcConsumerApplication.class, args);
    }
}
