package com.rpc.example.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ReqHolder {
    public static final AtomicLong REQUEST_ID = new AtomicLong();
    public static Map<Long, RpcFuture> REQUEST_MAP = new ConcurrentHashMap<>();
}
