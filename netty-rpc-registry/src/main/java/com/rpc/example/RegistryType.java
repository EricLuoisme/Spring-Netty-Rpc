package com.rpc.example;

public enum RegistryType {

    ZOOKEEPER((byte) 0),
    EUREKA((byte) 1);

    private byte code;

    RegistryType(byte code) {
        this.code = code;
    }

    public static RegistryType findByCode(int code) {
        for (RegistryType value : RegistryType.values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }
}
