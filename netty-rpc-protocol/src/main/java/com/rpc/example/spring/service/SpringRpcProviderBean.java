package com.rpc.example.spring.service;

import com.rpc.example.IRegistryService;
import com.rpc.example.ServiceInfo;
import com.rpc.example.annotation.RpcRemoteService;
import com.rpc.example.protocol.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 遍历Bean上注解的重要实现类
 * <p>
 * 通过接入Spring多个自动接口, 进行触发
 * 最主要是将开放RPC的Bean, 及其Method进行绑定, 在此提前放入Map, 与Spring容器的Bean处理手法类似
 */
@Slf4j
public class SpringRpcProviderBean implements InitializingBean, BeanPostProcessor {

    private final String serverAddress;
    private final int serverPort;

    // 服务注册用
    private final IRegistryService registryService;


//    public SpringRpcProviderBean(int serverPort) throws UnknownHostException {
//        String hostAddress = InetAddress.getLocalHost().getHostAddress();
//        log.info("------------ The Ip for this Server is " + hostAddress + " -------------------");
//        this.serverAddress = hostAddress;
//        this.serverPort = serverPort;
//    }

    public SpringRpcProviderBean(int serverPort, IRegistryService registryService) throws UnknownHostException {
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        log.info("------------ The Ip for this Server is " + hostAddress + " -------------------");
        this.serverAddress = hostAddress;
        this.serverPort = serverPort;
        this.registryService = registryService;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("===== Begin Deploy Netty-Server to host");
        // 另开一条线程来异步启动
        new Thread(() -> new NettyServer(this.serverAddress, this.serverPort).startNettyServer())
                .start();
    }

    /**
     * 动态代理, 服务注册, 其Spring容器内的切入点,
     * 每个Bean初始化方法调用后, 都会被回调,
     * 对所有需要处理额外处理bean的操作, 都可用用这个作为切入点
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 只要Bean声明了该注解, 就需要把服务发布
        if (bean.getClass().isAnnotationPresent(RpcRemoteService.class)) {
            Method[] declaredMethods = bean.getClass().getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                if ("private".equalsIgnoreCase(Modifier.toString(declaredMethod.getModifiers()))) {
                    continue;
                }
                String serviceName = bean.getClass().getInterfaces()[0].getName();
                String key = bean.getClass().getInterfaces()[0].getName() + "." + declaredMethod.getName();
                BeanMethod bm = new BeanMethod();
                bm.setBean(bean);
                bm.setMethod(declaredMethod);
                // 将这些方法和其对象实例保存
                Mediator.beanMethodMap.put(key, bm);

                // 服务注册到注册中心上
                ServiceInfo serviceInfo = new ServiceInfo();
                serviceInfo.setServiceAddress(this.serverAddress);
                serviceInfo.setServicePort(this.serverPort);
                serviceInfo.setServiceName(serviceName);
                registryService.register(serviceInfo);
            }
        }
        return bean;
    }
}
