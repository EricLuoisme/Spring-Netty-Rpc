package com.rpc.example.protocol;

import com.rpc.example.IRegistryService;
import com.rpc.example.ServiceInfo;
import com.rpc.example.core.RpcProtocol;
import com.rpc.example.core.RpcRequest;
import com.rpc.example.handler.RpcClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClient {

    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
//    private String serviceAddress;
//    private int servicePort;

//    public NettyClient(String serviceAddress, int servicePort) {
//        log.info("====================Begin Starting Netty-Client======================");
//        this.serviceAddress = serviceAddress;
//        this.servicePort = servicePort;
//
//        this.bootstrap = new Bootstrap();
//        bootstrap.group(eventLoopGroup)
//                .channel(NioSocketChannel.class)
//                .handler(new RpcClientInitializer());
//    }

    public NettyClient() {
        log.info("====================Begin Starting Netty-Client======================");

        this.bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new RpcClientInitializer());
    }


    /**
     * 使用Netty的Channel连接, 并发送请求 (异步连接)
     * 增强版: 请求zk获取服务访问地址
     */
    public void sendRequest(RpcProtocol<RpcRequest> protocol, IRegistryService registryService) {
        try {
            // 询问注册中心, 获取服务地址
            ServiceInfo serviceInfo = registryService.discovery(protocol.getContent().getClassName());
            // 建立连接, 需要同步等待
            ChannelFuture future = bootstrap.connect(
                            serviceInfo.getServiceAddress(), serviceInfo.getServicePort())
                    .sync();

            future.addListener(listener -> {
                if (future.isSuccess()) {
                    log.info("Connect Netty-Server successfully");
                } else {
                    log.error("Fail to connect to the Netty-Server");
                    future.cause().printStackTrace();
                    eventLoopGroup.shutdownGracefully();
                }
            });
            future.channel().writeAndFlush(protocol);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
