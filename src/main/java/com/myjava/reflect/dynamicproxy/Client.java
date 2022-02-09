package com.myjava.reflect.dynamicproxy;

/**
 * @Author lidexiu
 */
public class Client {
    public static void main(String[] args) {
        Host host = new Host();
        ProxyInvocationHandler proxyInvocationHandler = new ProxyInvocationHandler();
        proxyInvocationHandler.setObj(host);
        Rent rent = (Rent)proxyInvocationHandler.getProxy();
        rent.rent();

    }
}
