package com.rpc.example.handler;

import com.rpc.example.codec.RpcDecoder;
import com.rpc.example.codec.RpcEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
                12, 4, 0, 0))
                .addLast(new LoggingHandler())
                .addLast(new RpcEncoder())
                .addLast(new RpcDecoder())
                .addLast(new RpcClientHandler());
    }
}
