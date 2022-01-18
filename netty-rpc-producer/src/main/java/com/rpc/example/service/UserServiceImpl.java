package com.rpc.example.service;

import com.rpc.example.IUserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserServiceImpl implements IUserService {
    @Override
    public String saveUser(String name) {
        log.info("begin save user:{}", name);
        return "save user success " + name;
    }
}
