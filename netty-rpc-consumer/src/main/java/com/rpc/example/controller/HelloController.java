package com.rpc.example.controller;

import com.rpc.example.IUserService;
import com.rpc.example.annotation.RpcRemoteReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RpcRemoteReference
    IUserService userService;

    @GetMapping("/say")
    public String say() {
        return userService.saveUser("Robot X");
    }
}
