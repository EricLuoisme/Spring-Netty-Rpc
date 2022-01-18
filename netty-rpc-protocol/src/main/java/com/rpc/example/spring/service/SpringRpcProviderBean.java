package com.rpc.example.spring.service;

import com.rpc.example.annotation.RpcRemoteService;
import com.rpc.example.protocol.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 通过接入Spring多个自动接口, 进行触发
 * 最主要是将开放RPC的Bean, 及其Method进行绑定, 在此提前放入Map, 与Spring容器的Bean处理手法类似
 */
@Slf4j
public class SpringRpcProviderBean implements InitializingBean, BeanPostProcessor {

    private final String serverAddress;
    private final int serverPort;


    public SpringRpcProviderBean(int serverPort) throws UnknownHostException {
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        log.info("------------ The Ip for this Server is " + hostAddress + " -------------------");
        this.serverAddress = hostAddress;
        this.serverPort = serverPort;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("===== Begin Deploy Netty-Server to host");
        // 另开一条线程来异步启动
        new Thread(() -> new NettyServer(this.serverAddress, this.serverPort).startNettyServer())
                .start();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 只要Bean声明了该注解, 就需要把服务发布
        if (bean.getClass().isAnnotationPresent(RpcRemoteService.class)) {
            Method[] declaredMethods = bean.getClass().getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                String key = bean.getClass().getInterfaces()[0].getName() + "." + declaredMethod.getName();
                BeanMethod bm = new BeanMethod();
                bm.setBean(bean);
                bm.setMethod(declaredMethod);
                // 将这些方法和其对象实例保存
                Mediator.beanMethodMap.put(key, bm);
            }
        }
        return bean;
    }
}
