package com.flink.utils;

import com.flink.utls.YmlUtils;

import java.util.Map;

/**
 * @author ldx
 * @date 2022/9/23
 */
public class YmlUtilsTest {

    public static void main(String[] args) throws Exception {
        String ip = (String) YmlUtils.getAssignYmlProperties("job-conf", "socket.ip");
        Integer port = (Integer) YmlUtils.getAssignYmlProperties("job-conf", "socket.port");
        System.out.println("ip:" + ip + ",port:" + port);

    }
}
