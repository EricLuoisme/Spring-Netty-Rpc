package com.rpc.example.serial;

public interface ISerializer {

    /**
     * 序列化
     */
    <T> byte[] serialize(T obj);

    /**
     * 反序列化
     */
    <T> T deserialize(byte[] data, Class<T> clazz);

    byte getType();
}
