package com.myjava.fanxing;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lidexiu
 * @Date 2021/12/1
 * @Description
 */
public class Test {
    public static void main(String[] args) {
        List<Number> list = new ArrayList<Number>();
        list.add(new Integer(123));
        list.add(new Double(12.34));
        Number first = list.get(0);
        Number second = list.get(1);
    }
}
