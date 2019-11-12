package com.zfzj.service;

import com.zfzj.protocol.RpcRequest;
import com.zfzj.protocol.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;

/**
 * @author jeremy.wang
 * @date 2019-11-05
 */
public class ServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        RpcResponse rpcResponse = null;
        try {
            rpcResponse = new RpcResponse();
            rpcResponse.setRequestId(msg.getRequestId());
            rpcResponse.setResult(reflectObject(msg));
        } catch (Throwable throwable) {
            rpcResponse.setThrowable(throwable);
            throwable.printStackTrace();
        }
        ctx.writeAndFlush(rpcResponse);
    }

    private Object reflectObject(RpcRequest msg) throws Throwable {
        String methodName = msg.getMethodName();
        Class<?>[] parameterTypes = msg.getParameterTypes();
        Object[] arguments = msg.getParameters();

        Method method = msg.getClass().getMethod(methodName, parameterTypes);
        Object service = ExportService.applicationContext.get(msg.getClassName());
        Object result = method.invoke(service, arguments);
        return result;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
