package com.zfzj.service.reflect;

import java.lang.reflect.Method;

/**
 * @author jeremy.wang
 * @date 2019-11-13
 */
public class ReflectTest {

    public static void main(String[] args) throws Exception {
        String className = "com.zfzj.service.reflect.People";

        Class<?> clz = Class.forName(className);
        Object instance = clz.newInstance();
        String methodName = "getFullName";
        Class<?>[] parameterTypes = {String.class, String.class};
        Object[] arguments = {"jeremy", "wang"};

        Method method = clz.getMethod(methodName, parameterTypes);
        Object result = method.invoke(instance, arguments);
        System.out.println(result);
    }
}
