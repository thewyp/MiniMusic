package com.thewyp.lib.java;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class PrinterProxy implements InvocationHandler {

    private Object target;

    public Object newProxyInstance(Object object) {
        target = object;
        return Proxy.newProxyInstance(
                object.getClass().getClassLoader(),
                object.getClass().getInterfaces(),
                this);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

        System.out.println("记录日志");

        return method.invoke(target, objects);
    }
}
