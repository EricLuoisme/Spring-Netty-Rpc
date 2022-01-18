package com.rpc.example.handler;

import com.rpc.example.constants.ReqType;
import com.rpc.example.core.Header;
import com.rpc.example.core.RpcProtocol;
import com.rpc.example.core.RpcRequest;
import com.rpc.example.core.RpcResponse;
import com.rpc.example.spring.SpringBeanManager;
import com.rpc.example.spring.service.Mediator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RpcServerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> msg) throws Exception {
        // 组装返回报文
        RpcProtocol<RpcResponse> resProtocol = new RpcProtocol<>();
        Header header = msg.getHeader();
        header.setReqType(ReqType.RESPONSE.getCode());

        // 通过反射等, 调用并得到结果
        Object result = Mediator.getInstance().processor(msg.getContent());
//        Object result = invoke(msg.getContent());

        resProtocol.setHeader(header);
        // 组装Response
        RpcResponse resp = new RpcResponse();
        resp.setData(result);
        resp.setMsg("success");
        resProtocol.setContent(resp);

        // flush写出去
        ctx.writeAndFlush(resProtocol);
    }

    /**
     * 1. 根据Class名称获取Class类对象
     * 2. 根据类对象名称, 从ApplicationContext获取它的一个实例
     * 3. 再根据方法名称, 以及这个实例, 进行具体方法的反射调用
     */
    @Deprecated
    private Object invoke(RpcRequest request) {
        try {
            Class<?> aClass = Class.forName(request.getClassName());
            Object bean = SpringBeanManager.getBean(aClass);
            Method declaredMethod = aClass.getDeclaredMethod(request.getMethodName(), request.getParamsTypes());
            return declaredMethod.invoke(bean, request.getParams());
        } catch (ClassNotFoundException | NoSuchMethodException
                | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
