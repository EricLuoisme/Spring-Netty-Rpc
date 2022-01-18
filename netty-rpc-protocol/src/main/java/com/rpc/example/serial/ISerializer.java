package com.rpc.example.serial;

public interface ISerializer<T> {

    /**
     * 序列化
     */
    byte[] serialize(T obj);

    /**
     * 反序列化
     */
    T deserialize(byte[] data, Class<T> clazz);

    byte getType();
}
