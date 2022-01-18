package com.rpc.example.core;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Header implements Serializable {

    /** 魔数 2-Bytes */
    private short magic;

    /** 序列化类型 1-Bytes */
    private byte serialType;

    /** 请求类型 1-Bytes */
    private byte reqType;

    /** 请求id 8-Bytes */
    private long reqId;

    /** 消息体长度 4-Bytes */
    private int length;
}
