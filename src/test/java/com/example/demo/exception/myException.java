package com.example.demo.exception;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @Author lidexiu
 * @Date 2021/7/16
 * @Description
 */
@Slf4j
public class myException {

    public void exceptionInit(boolean throwException){
        if (throwException){
            throw new RuntimeException("运行时异常");
        }
        else {

        }
    }

    @Test
    public void execptionTest(){
        exceptionInit(true);
    }



}
