package com.example.demo.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.example.demo.utils.NetWorkUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketException;

/**
 * @Author lidexiu
 */
@Slf4j
public class LogIpConfig  extends ClassicConverter {
    private static String localIP;
    static {
        try {
            localIP = NetWorkUtil.getLocalIP();
        } catch (SocketException e) {
            log.error("获取本机ip失败", e);
            localIP = null;
        }
    }
    @Override
    public String convert(ILoggingEvent event) {
        return localIP;
    }
}
