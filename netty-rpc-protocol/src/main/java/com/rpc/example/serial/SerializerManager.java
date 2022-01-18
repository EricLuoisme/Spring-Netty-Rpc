package com.rpc.example.serial;

import java.util.concurrent.ConcurrentHashMap;

public class SerializerManager {
    private final static ConcurrentHashMap<Byte, ISerializer> serializer = new ConcurrentHashMap<>();

    static {
        ISerializer json = new JsonSerializer();
        ISerializer java = new JavaSerializer();
        serializer.put(json.getType(), json);
        serializer.put(java.getType(), java);
    }

    public static ISerializer getSerializer(byte key) {
        ISerializer iSerializer = serializer.get(key);
        if (null == iSerializer) {
            return new JavaSerializer();
        }
        return iSerializer;
    }
}
