package com.rpc.example.core;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcRequest implements Serializable {

    /**
     * 反射调用远程服务需要的内容
     */

    private String className;
    private String methodName;
    private Object[] params;
    private Class<?>[] paramsTypes;
}
