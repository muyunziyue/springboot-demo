package com.myjava.multithread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author ldx
 */
public class Main {
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));

    }
}
