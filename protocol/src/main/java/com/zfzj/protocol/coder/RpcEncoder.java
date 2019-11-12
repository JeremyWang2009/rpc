package com.zfzj.protocol.coder;

import com.zfzj.protocol.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author jeremy.wang
 * @date 2019-11-12
 */
public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> clz;

    private Serialization serialization;

    public RpcEncoder(Class<?> clz, Serialization serialization) {
        this.clz = clz;
        this.serialization = serialization;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf byteBuf)
            throws Exception {
        if (clz != null) {
            byte[] bytes = serialization.serialize(msg);
            byteBuf.writeInt(bytes.length);
            byteBuf.writeBytes(bytes);
        }
    }
}
