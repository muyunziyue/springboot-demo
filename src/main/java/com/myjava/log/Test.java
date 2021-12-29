package com.myjava.log;

import com.example.demo.utils.NetWorkUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketException;

/**
 * @Author lidexiu
 */
@Slf4j
public class Test {
    public static void main(String[] args) throws SocketException {
      log.info("测试日志是否携带ip" + System.currentTimeMillis());
        String localIP = NetWorkUtil.getLocalIP();
    }
}
