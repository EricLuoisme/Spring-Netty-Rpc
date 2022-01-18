package com.rpc.example.constants;

public enum SerialType {

    JSON_SERIAL((byte) 1),
    JAVA_SERIAL((byte) 2);

    private byte code;

    SerialType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
