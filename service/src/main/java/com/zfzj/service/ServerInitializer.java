package com.zfzj.service;

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
 * @date 2019-11-07
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(new RpcEncoder(RpcResponse.class, new JsonSerialization()));
        pipeline.addLast(new RpcDecoder(RpcRequest.class, new JsonSerialization()));
        pipeline.addLast(new ServerHandler());
    }
}
