package com.example.demo.protocol;

import org.junit.jupiter.api.Test;

/**
 * @Author lidexiu
 * @Date 2021/7/6
 * @Description
 */
public class CollectionProtocolTest {

    @Test
    public void caseWhenNullTest() {

        String param = null;

        switch (param == null ? "null" : param) {
            // 肯定不是进入这里
            case "sth":
                System.out.println("it's sth");
                break;
            // 也不是进入这里
            case "null":
                System.out.println("it's null");
                break;
            // 也不是进入这里
            default:
                System.out.println("default");
        }
    }

    @Test
    public void sanmu() {
        Integer a = 1;
        Integer b = 2;
        Integer c = null;
        Boolean flag = false;

        System.out.println(flag ? a * b : c);
    }
}

