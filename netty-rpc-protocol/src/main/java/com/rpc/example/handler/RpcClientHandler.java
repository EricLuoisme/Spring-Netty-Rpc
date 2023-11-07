package com.rpc.example.handler;

import com.rpc.example.core.ReqHolder;
import com.rpc.example.core.RpcFuture;
import com.rpc.example.core.RpcProtocol;
import com.rpc.example.core.RpcResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * 可以仅处理某种类型 (从RpcDecoder中decode完毕的类型) 的handler
 */
@Slf4j
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcResponse> msg) throws Exception {
        log.info("Receive Response from Netty-Server");
        long reqId = msg.getHeader().getReqId();
        RpcFuture future = ReqHolder.REQUEST_MAP.remove(reqId);
        future.getPromise()
                // notify all listeners
                .setSuccess(msg.getContent());
    }
}
