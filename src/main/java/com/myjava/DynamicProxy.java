package com.myjava;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author lidexiu
 * @Date 2021/12/1
 * @Description
 */
public class DynamicProxy {
    public static void main(String[] args) {
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(method);
                if ("morning".equals(method.getName())){
                    System.out.println("invoke morning method" + args[0]);
                }

                return null;
            }
        };
        Hello hello = (Hello) Proxy.newProxyInstance(Hello.class.getClassLoader(), new Class[]{Hello.class}, invocationHandler);
        hello.morning("zhangsan");

    }

    interface Hello{
        void morning(String name);
    }
}
