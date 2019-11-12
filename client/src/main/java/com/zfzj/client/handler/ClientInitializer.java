package com.zfzj.client.handler;

import com.zfzj.protocol.JsonSerialization;
import com.zfzj.protocol.RpcRequest;
import com.zfzj.protocol.RpcResponse;
import com.zfzj.protocol.coder.RpcDecoder;
import com.zfzj.protocol.coder.RpcEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;

/**
 * @author jeremy.wang
 * @date 2019-11-05
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    private static final ClientHandler CLIENT_HANDLER = new ClientHandler();

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(new RpcEncoder(RpcRequest.class, new JsonSerialization()));
        pipeline.addLast(new RpcDecoder(RpcResponse.class, new JsonSerialization()));
        pipeline.addLast(CLIENT_HANDLER);
    }
}
