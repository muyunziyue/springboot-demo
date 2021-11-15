package com.myjava.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author lidexiu
 * @Date 2021/11/15
 * @Description
 */
public class MyExceptionTest {
    @Autowired
    MyException myException;

    @Test
    public void exceptionTest() throws MyException {
        throwException(false);
    }

    private void throwException(Boolean isThrown) throws MyException {
        if (isThrown){
            throw new MyException("测试自定义异常");
        }
        else {
            //do nothing
        }
    }
}
