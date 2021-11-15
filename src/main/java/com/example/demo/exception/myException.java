package com.example.demo.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author lidexiu
 * @Date 2021/7/16
 * @Description
 */
@Slf4j
public class myException {

    public static void  exceptionInit(boolean throwException) {
        if (throwException) {
            log.info("异常抛出前");
            throw new RuntimeException("运行时异常");
        } else {
            log.info("正常运行");
            if (log.isDebugEnabled()) {
                log.debug("debug日志输出");
            }
        }
    }

    public static void main(String[] args) {

        log.debug("debug-----main");

        try {
            exceptionInit(true);
        } catch (RuntimeException e) {
            log.warn("捕获运行时异常",e);
            log.debug("debug日志输出");
            if (log.isDebugEnabled()) {
                log.debug("debug日志输出");
            }
        }
        log.info("捕获异常后日志记录");
    }
}




