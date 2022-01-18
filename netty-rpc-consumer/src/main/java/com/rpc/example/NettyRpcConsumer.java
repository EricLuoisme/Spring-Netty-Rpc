package com.rpc.example;

public class NettyRpcConsumer {

    public static void main(String[] args) {
        RpcClientProxy rpc = new RpcClientProxy();
//        IUserService userService = rpc.clientProxy(IUserService.class, "localhost", 8080);
        IUserService userService = rpc.clientProxy(IUserService.class, "172.29.128.1", 8888);
        System.out.println(userService.saveUser("Test User"));
    }

}
