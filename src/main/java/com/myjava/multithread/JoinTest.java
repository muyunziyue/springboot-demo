package com.myjava.multithread;

/**
 * @Author lidexiu
 * @Date 2021/12/7
 * @Description
 */
public class JoinTest {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(()->{

            System.out.println("hello " + Thread.currentThread().getName());
        });

        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("hello " + Thread.currentThread().getName());
        });
        thread1.start();
        thread2.start();
        System.out.println("main start");
        thread2.join();
        System.out.println("main end");

    }
}
