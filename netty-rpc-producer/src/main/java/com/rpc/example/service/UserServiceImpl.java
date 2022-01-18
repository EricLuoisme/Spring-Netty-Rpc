package com.rpc.example.service;

import com.rpc.example.IUserService;
import com.rpc.example.annotation.RpcRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RpcRemoteService
public class UserServiceImpl implements IUserService {
    @Override
    public String saveUser(String name) {
        log.info("begin save user:{}", name);
        return "save user success " + name;
    }
}
