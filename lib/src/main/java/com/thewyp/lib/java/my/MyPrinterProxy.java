package com.thewyp.lib.java.my;

import java.lang.reflect.Method;

public class MyPrinterProxy implements MyInvocationHandler {

    private Object target;

    public Object newProxyInstance(Object object) {
        target = object;
        return MyProxy.newProxyInstance(
                new MyClassLoader(),
                object.getClass().getInterfaces(),
                this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("自定义代理，记录日志");
        return method.invoke(target, args);
    }
}
