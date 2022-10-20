package com.flink.stream.watermark.lesson03;

public class Test {
    public static void main(String[] args) {
        String currTime = String.valueOf(1595926224487L);
        int r=Integer.valueOf(currTime.substring(currTime.length() - 4));
        System.out.println(r);
    }
}
