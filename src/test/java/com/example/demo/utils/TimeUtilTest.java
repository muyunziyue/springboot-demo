package com.example.demo.utils;

import org.junit.Test;

/**
 * @Author lidexiu
 * @Date 2021/12/29
 * @Description
 */
public class TimeUtilTest {
    @Test
    public void getFormatTest() throws Exception {
        String format = TimeUtil.getFormat("2021-01-01 23:34:11");
        System.out.println(format);
    }
}
