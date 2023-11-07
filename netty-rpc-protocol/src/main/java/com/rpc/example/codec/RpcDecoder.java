package com.rpc.example.codec;

import com.rpc.example.constants.ReqType;
import com.rpc.example.constants.RpcConstants;
import com.rpc.example.core.Header;
import com.rpc.example.core.RpcProtocol;
import com.rpc.example.core.RpcRequest;
import com.rpc.example.core.RpcResponse;
import com.rpc.example.serial.ISerializer;
import com.rpc.example.serial.SerializerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 由于网络传输的只是Byte数组, 使用Netty框架传输的也是ByteBuf,
 * 所以需要根据传输的Protocol, 将Byte[]转换回对象实例
 */
@Slf4j
public class RpcDecoder extends ByteToMessageDecoder {

    /**
     * Decoder 可以进行2种区分,
     * 区分目前decode的是Request还是Response, 因为2者结构可能不同, 后续decode完毕是
     * RpcRequest & RpcResponse 两种, add到out中让后续handler处理
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        log.info("============begin RpcDecoder==========");
        if (in.readableBytes() < RpcConstants.HEAD_TOTOAL_LEN) {
            // 小于标准头部字节数, 丢弃
            return;
        }
        // mark 读数据开始位置
        in.markReaderIndex();

        // 校验Magic Num
        short mgc = in.readShort();
        if (mgc != RpcConstants.MAGIC) {
            throw new IllegalAccessException("Illegal request param 'magic', " + mgc);
        }

        // 读一个字节的序列化类型
        byte serialType = in.readByte();
        // 读一个字节的消息类型
        byte reqType = in.readByte();
        // 读8个字节的请求id
        long reqId = in.readLong();
        // 读4个字节的消息体长度
        int msgLen = in.readInt();

        // 数据处理
        if (in.readableBytes() < msgLen) {
            // 存在问题, 重置回去mark的位置, 丢弃
            in.resetReaderIndex();
            return;
        }
        // 将消息体读出放入Byte数组
        byte[] content = new byte[msgLen];
        in.readBytes(content);

        // 根据请求类型, 准备反序列化为具体对象
        Header header = new Header(mgc, serialType, reqType, reqId, msgLen);
        ISerializer serializer = SerializerManager.getSerializer(serialType);
        ReqType byCode = ReqType.findByCode(reqType);

        switch (byCode) {
            case REQUEST:
                // 组装包裹Request的Protocol对象实例
                RpcRequest request = serializer.deserialize(content, RpcRequest.class);
                RpcProtocol<RpcRequest> reqProtocol = new RpcProtocol<>();
                reqProtocol.setHeader(header);
                reqProtocol.setContent(request);
                // 添加到调用链
                out.add(reqProtocol);
                break;
            case RESPONSE:
                // 组装包裹Request的Protocol对象实例
                RpcResponse response = serializer.deserialize(content, RpcResponse.class);
                RpcProtocol<RpcResponse> respProtocol = new RpcProtocol<>();
                respProtocol.setHeader(header);
                respProtocol.setContent(response);
                // 添加到调用链
                out.add(respProtocol);
                break;
            case HEARTBEAT:
                // TODO
                break;
            default:
                break;
        }

    }
}
