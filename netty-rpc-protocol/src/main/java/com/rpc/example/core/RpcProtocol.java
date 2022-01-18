package com.rpc.example.core;

import lombok.Data;

import java.io.Serializable;

/**
 * 可用来包裹Request或Response进行传输
 * @param <T> RpcRequest / RpcResponse
 */
@Data
public class RpcProtocol<T> implements Serializable {
    private Header header;
    private T content;
}
