package com.example.demo.utils;

import org.junit.Test;

import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @Author lidexiu
 */
public class NetWorkUtilTest {

    @Test
    public void getLocalIPTest() throws SocketException {
        String localIP = NetWorkUtil.getLocalIP();
        System.out.println(localIP);
    }

    @Test
    public void getLocalHostName() throws UnknownHostException {
        String localHostName = NetWorkUtil.getLocalHostName();
        System.out.println(localHostName);
    }
}
