package com.rpc.example;

import com.rpc.example.constants.ReqType;
import com.rpc.example.constants.RpcConstants;
import com.rpc.example.constants.SerialType;
import com.rpc.example.core.*;
import com.rpc.example.protocol.NettyClient;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 使用JDK接口实现的动态代理
 * 调用特定方法时, 会协助远程RPC获取结果
 */
@Deprecated
@Slf4j
@AllArgsConstructor
public class RpcInvokerProxy implements InvocationHandler {
//
//    private String host;
//    private int port;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

//        log.info("================Begin invoke Rpc====================");
//        // 组装请求
//        RpcProtocol<RpcRequest> reqProtocol = new RpcProtocol<>();
//        long reqId = ReqHolder.REQUEST_ID.incrementAndGet();
//        Header header = new Header(RpcConstants.MAGIC, SerialType.JSON_SERIAL.getCode(),
//                ReqType.REQUEST.getCode(), reqId, 0);
//        reqProtocol.setHeader(header);
//
//        // 远程Proxy反射调用所需内容
//        RpcRequest req = new RpcRequest();
//        req.setClassName(method.getDeclaringClass().getName());
//        req.setMethodName(method.getName());
//        req.setParamsTypes(method.getParameterTypes());
//        req.setParams(args);
//        reqProtocol.setContent(req);
//
//        // 进行发送
//        NettyClient client = new NettyClient(host, port);
//        // 由于Netty请求与响应都是异步的, 这里使用Netty实现的Future, 轮询获取结果
//        RpcFuture<RpcResponse> future = new RpcFuture<>(new DefaultPromise<>(new DefaultEventLoop()));
//        // 提前将reqId和可能返回的future进行绑定, 放入map中
//        ReqHolder.REQUEST_MAP.put(reqId, future);
//        client.sendRequest(reqProtocol);
//        // get也就是同步阻塞, 直到它有值
//        return future.getPromise()
//                .get().getData();
        return null;
    }
}
