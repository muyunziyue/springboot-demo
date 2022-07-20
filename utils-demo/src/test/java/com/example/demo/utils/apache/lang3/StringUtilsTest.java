package com.example.demo.utils.apache.lang3;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * @author ldx
 * @date 2022/7/12
 */
public class StringUtilsTest {

    @Test
    public void BlankTest(){
        String str = " ";
        System.out.println(StringUtils.isBlank(str));
        System.out.println(StringUtils.isNotBlank(str));
    }
}
