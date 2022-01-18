package com.rpc.example.core;

import lombok.Data;

import java.io.Serializable;

/**
 * 反射调用远程服务需要的内容
 */
@Data
public class RpcRequest implements Serializable {

    /** 远程调用的Class名称 */
    private String className;

    /** 远程调用的方法名称 */
    private String methodName;

    /** 远程调用的方法的参数类型 */
    private Class<?>[] paramsTypes;

    /** 远程调用的方法入参 */
    private Object[] params;
}
