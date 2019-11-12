package com.zfzj.service;

import com.zfzj.service.service.HelloService;
import com.zfzj.service.service.HelloServiceImpl;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author jeremy.wang
 * @date 2019-11-04
 */
public class RpcFrameworkTest {

    /**
     * 暴露服务
     *
     * @param service
     * @param port
     * @throws IOException
     */
    public static void export(final Object service, int port) throws IOException {
        if (service == null) {
            throw new IllegalArgumentException("export service instance is null.");
        }
        if (port <= 0 || port >= 65535) {
            throw new IllegalArgumentException("export invalid port " + port);
        }

        final ServerSocket server = new ServerSocket(port);
        for (; ; ) {
            try {
                final Socket socket = server.accept();
                new Thread(
                                new Runnable() {
                                    public void run() {
                                        try {
                                            ObjectOutputStream output = null;
                                            ObjectInputStream input = null;
                                            try {
                                                input =
                                                        new ObjectInputStream(
                                                                socket.getInputStream());
                                                output =
                                                        new ObjectOutputStream(
                                                                socket.getOutputStream());

                                                String methodName = input.readUTF();
                                                Class<?>[] parameterTypes =
                                                        (Class<?>[]) input.readObject();
                                                Object[] arguments = (Object[]) input.readObject();

                                                Method method =
                                                        service.getClass()
                                                                .getMethod(
                                                                        methodName, parameterTypes);
                                                Object result = method.invoke(service, arguments);
                                                output.writeObject(result);
                                            } catch (IOException
                                                    | ClassNotFoundException
                                                    | NoSuchMethodException
                                                    | SecurityException
                                                    | IllegalAccessException
                                                    | IllegalArgumentException
                                                    | InvocationTargetException e) {
                                                e.printStackTrace();
                                            } finally {
                                                if (input != null) {
                                                    input.close();
                                                }
                                                if (output != null) {
                                                    output.close();
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                        .start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 引用服务
     *
     * @param interfaceClass
     * @param host
     * @param port
     * @param <T>
     * @return
     */
    public static <T> T refer(final Class<T> interfaceClass, final String host, final int port) {
        if (interfaceClass == null) {
            throw new IllegalArgumentException("refer interfaceClass is null.");
        }
        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException(
                    "refer interfaceClass "
                            + interfaceClass.getName()
                            + " must be interface class.");
        }
        if (host == null || host.length() == 0) {
            throw new IllegalArgumentException("refer host is null.");
        }
        if (port <= 0 || port >= 65535) {
            throw new IllegalArgumentException("export invalid port " + port);
        }
        return (T)
                Proxy.newProxyInstance(
                        interfaceClass.getClassLoader(),
                        new Class<?>[] {interfaceClass},
                        new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args)
                                    throws Throwable {
                                Socket socket = null;
                                ObjectOutputStream output = null;
                                ObjectInputStream input = null;
                                Object result = null;
                                try {
                                    socket = new Socket(host, port);
                                    output = new ObjectOutputStream(socket.getOutputStream());
                                    input = new ObjectInputStream(socket.getInputStream());

                                    output.writeUTF(method.getName());
                                    output.writeObject(method.getParameterTypes());
                                    output.writeObject(args);

                                    result = input.readObject();
                                    if (result instanceof Throwable) {
                                        throw (Throwable) result;
                                    }
                                } catch (IOException | ClassNotFoundException e) {
                                    e.printStackTrace();
                                } finally {
                                    try {
                                        if (socket != null) {
                                            socket.close();
                                        }
                                        if (output != null) {
                                            output.close();
                                        }
                                        if (input != null) {
                                            input.close();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                return result;
                            }
                        });
    }

    @Test
    public void producer() throws IOException {
        HelloService service = new HelloServiceImpl();
        RpcFrameworkTest.export(service, 1234);
    }

    @Test
    public void consumer() {
        HelloService helloService = RpcFrameworkTest.refer(HelloService.class, "127.0.0.1", 1234);
        String hello = helloService.hello("jeremy");
        System.out.println(hello);
    }
}
