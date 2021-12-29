package com.myjava;

import org.apache.lucene.util.RamUsageEstimator;

/**
 * @Author lidexiu
 * @Date 2021/12/1
 * @Description
 */
public class ObjectSize {
    public static void main(String[] args) {
        Integer integer = 999;
        long size = RamUsageEstimator.shallowSizeOf(integer);

        System.out.println(size);
    }
}
