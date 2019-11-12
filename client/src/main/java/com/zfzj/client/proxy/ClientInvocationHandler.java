package com.zfzj.client.proxy;

import com.zfzj.client.handler.NettyClient;
import com.zfzj.protocol.RpcRequest;
import com.zfzj.protocol.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author jeremy.wang
 * @date 2019-11-12
 */
public class ClientInvocationHandler<T> implements InvocationHandler {

    private Class<T> clz;

    public ClientInvocationHandler(Class<T> clz) {
        this.clz = clz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = new RpcRequest();
        String requestId = UUID.randomUUID().toString();

        request.setRequestId(requestId);
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);

        NettyClient nettyClient = new NettyClient("127.0.0.1", 8889);
        nettyClient.connect();
        RpcResponse rpcResponse = nettyClient.send(request);
        return rpcResponse;
    }
}
