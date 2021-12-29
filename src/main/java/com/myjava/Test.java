package com.myjava;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.net.URISyntaxException;

/**
 * @Author lidexiu
 * @Date 2021/12/8
 * @Description
 */
@Slf4j
public class Test {
    public static void main(String[] args) throws URISyntaxException {
//        send("测试消息={}", null);
        try {
            exceptionTest(4);
        } catch (Exception e) {
            log.error("error in main method", e);
        }
        System.out.println("end");
    }

    public static void send(String msg, String... params) {
        if (StringUtils.isBlank(msg)) {
            log.error("钉钉报警消息体为空");
            return;
        }
        String split = msg.replaceAll("\\{\\}", "{%s}");
        String formatMsg = String.format(String.join("{%s}", split), params);
        send(formatMsg);
    }

    public static void send(String msg) {
        System.out.println("msg=" + msg);
    }

    public static void exceptionTest(int i) throws Exception {
            if (i % 2 == 0) {
                int i1 = i / 0;
            }else {
                int i1 = i;
            }
    }


    class MyException extends RuntimeException {
        public MyException() {
            super();
        }

        public MyException(String message) {
            super(message);
        }

        public MyException(String message, Throwable cause) {
            super(message, cause);
        }

        public MyException(Throwable cause) {
            super(cause);
        }

        protected MyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}

