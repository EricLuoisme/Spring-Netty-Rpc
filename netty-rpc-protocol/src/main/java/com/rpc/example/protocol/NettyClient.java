package com.rpc.example.protocol;

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
    private String serviceAddress;
    private int servicePort;

    public NettyClient(String serviceAddress, int servicePort) {
        log.info("====================Begin Starting Netty-Client======================");
        this.serviceAddress = serviceAddress;
        this.servicePort = servicePort;

        this.bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new RpcClientInitializer());
    }


    /**
     * 使用Netty的Channel连接, 并发送请求 (异步连接)
     */
    public void sendRequest(RpcProtocol<RpcRequest> protocol) {
        try {
            // 建立连接, 需要同步等待
            ChannelFuture future = bootstrap.connect(this.serviceAddress, this.servicePort).sync();
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
