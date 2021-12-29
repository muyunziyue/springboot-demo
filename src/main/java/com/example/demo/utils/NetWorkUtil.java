package com.example.demo.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @Author lidexiu
 */
@Slf4j
public class NetWorkUtil {

    public static String getLocalIP() throws SocketException {
        if (isWindowsOS()) {
            return getWindowsIP();
        }
        return getLinuxLocalIp();
    }

    private static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    public static String getLocalHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    private static String getWindowsIP() {
        StringBuffer serverIP = new StringBuffer();
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        byte[] ipAddr = addr.getAddress();
        for (int i = 0; i < ipAddr.length; i++) {
            if (i > 0) {
                serverIP.append(".");
            }
            serverIP.append(ipAddr[i] & 0xFF);
        }
        return serverIP.toString();
    }

    private static String getLinuxLocalIp() throws SocketException {
        String ip = "";

        for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces
                .hasMoreElements(); ) {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                continue;
            }
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress inetAddress = addresses.nextElement();
                if (inetAddress.isSiteLocalAddress()) {
                    ip = inetAddress.getHostAddress();
                    return ip;
                }
            }
        }
        return ip;
    }
}

