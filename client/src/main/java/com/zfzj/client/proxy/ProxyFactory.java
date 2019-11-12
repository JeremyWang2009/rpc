package com.zfzj.client.proxy;

import java.lang.reflect.Proxy;

/**
 * @author jeremy.wang
 * @date 2019-11-12
 */
public class ProxyFactory {

    public static <T> T create(Class<T> interfaceClass) {
        return (T)
                Proxy.newProxyInstance(
                        interfaceClass.getClassLoader(),
                        new Class<?>[] {interfaceClass},
                        new ClientInvocationHandler<T>(interfaceClass));
    }
}
