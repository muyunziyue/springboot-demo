package com.example.demo.bit;

import java.util.Arrays;

/**
 * @Author lidexiu
 * @Date 2021/9/1
 * @Description
 */
public class BitTest {
    public static void main(String[] args) {
        Byte[] bytes = new Byte[2];

        Arrays.fill(bytes, (byte)0);

        /**
         * 00000000 00000000
         */
        for (int i = 1; i < 17; i++) {
            int bitIndex = i % 8 - 1;
            int arrayIndex = i / 8;
            bytes[arrayIndex] = (byte)(Byte.toUnsignedInt(bytes[arrayIndex]) + 1<<bitIndex);

        }

    }

}
