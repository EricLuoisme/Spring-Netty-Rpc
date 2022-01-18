package com.rpc.example;

public class NettyRpcConsumer {

    public static void main(String[] args) {
        RpcClientProxy rpc = new RpcClientProxy();
        IUserService userService = rpc.clientProxy(IUserService.class, "localhost", 8080);
        System.out.println(userService.saveUser("Test User"));
    }

}
