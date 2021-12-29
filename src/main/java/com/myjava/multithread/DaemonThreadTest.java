package com.myjava.multithread;

import java.time.LocalTime;

/**
 * @Author lidexiu
 * @Date 2021/12/8
 * @Description
 */
public class DaemonThreadTest {
    public static void main(String[] args) {
        System.out.println(LocalTime.now());
        Thread thread = new Thread(() -> {
            while (true){
                System.out.println(LocalTime.now());
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

//        thread.setDaemon(true);
        thread.start();
    }
}
