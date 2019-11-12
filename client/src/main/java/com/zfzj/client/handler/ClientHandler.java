package com.zfzj.client.handler;

import com.zfzj.client.DefaultFuture;
import com.zfzj.protocol.RpcRequest;
import com.zfzj.protocol.RpcResponse;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jeremy.wang
 * @date 2019-11-05
 */
public class ClientHandler extends ChannelDuplexHandler {

    private final Map<String, DefaultFuture> futureMap = new ConcurrentHashMap<>();

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
            throws Exception {
        if (msg instanceof RpcRequest) {
            RpcRequest rpcRequest = (RpcRequest) msg;
            futureMap.putIfAbsent(rpcRequest.getRequestId(), new DefaultFuture());
            super.write(ctx, msg, promise);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcResponse) {
            RpcResponse rpcResponse = (RpcResponse)msg;
            DefaultFuture defaultFuture = futureMap.get(rpcResponse.getRequestId());
            defaultFuture.setResponse(rpcResponse);
            super.channelRead(ctx, msg);
        }
    }
}
