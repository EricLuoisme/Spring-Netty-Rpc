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
        // 写Header相关 (固定头部类型)
        Header header = msg.getHeader();
        out.writeShort(header.getMagic()); // magic number 确认是这个protocol
        out.writeByte(header.getSerialType()); // serialType简单的序列化类型
        out.writeByte(header.getReqType()); // 请求类型
        out.writeLong(header.getReqId()); // requestId
        // 写Content (这里就是序列化真正需要传输的信息, 序列化为bytes数组, Netty也是使用ByteBuf来传输的)
        ISerializer serializer = SerializerManager.getSerializer(header.getSerialType());
        byte[] contentBytes = serializer.serialize(msg.getContent());
        header.setLength(contentBytes.length); // 头部存入序列化的消息的长度, 实现更加精准的传输 (e.g. 参考ProtoBuf就是有带长度的)
        out.writeInt(contentBytes.length);
        out.writeBytes(contentBytes);
    }
}
