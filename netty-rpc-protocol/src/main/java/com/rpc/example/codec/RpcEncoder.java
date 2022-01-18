package com.rpc.example.codec;

import com.rpc.example.core.Header;
import com.rpc.example.core.RpcProtocol;
import com.rpc.example.serial.ISerializer;
import com.rpc.example.serial.SerializerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 由于网络传输的只是Byte数组, 使用Netty框架传输的也是ByteBuf,
 * 所以需要根据传输的实例, 转换为Byte[]
 */
@Slf4j
public class RpcEncoder extends MessageToByteEncoder<RpcProtocol<Object>> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcProtocol<Object> msg, ByteBuf out) throws Exception {
        log.info("============begin RpcEncoder==========");
        // 写Header相关
        Header header = msg.getHeader();
        out.writeShort(header.getMagic());
        out.writeByte(header.getSerialType());
        out.writeByte(header.getReqType());
        out.writeLong(header.getReqId());
        // 写Content
        ISerializer serializer = SerializerManager.getSerializer(header.getSerialType());
        byte[] contentBytes = serializer.serialize(msg.getContent());
        header.setLength(contentBytes.length);
        out.writeInt(contentBytes.length);
        out.writeBytes(contentBytes);
    }
}
