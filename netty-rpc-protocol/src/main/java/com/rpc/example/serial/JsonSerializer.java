package com.rpc.example.serial;

import com.alibaba.fastjson.JSON;
import com.rpc.example.constants.SerialType;

public class JsonSerializer implements ISerializer {

    @Override
    public byte[] serialize(Object obj) {
        return JSON.toJSONString(obj).getBytes();
    }

    @Override
    public Object deserialize(byte[] data, Class clazz) {
        return JSON.parseObject(new String(data), clazz);
    }

    @Override
    public byte getType() {
        return SerialType.JSON_SERIAL.getCode();
    }
}
