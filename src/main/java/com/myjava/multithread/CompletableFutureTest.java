package com.myjava.multithread;

import java.util.concurrent.CompletableFuture;

/**
 * @Author lidexiu
 * @Date 2021/12/15
 * @Description
 */
public class CompletableFutureTest {
    public static void main(String[] args) throws InterruptedException {
        CompletableFuture<Double> future = CompletableFuture.supplyAsync(CompletableFutureTest::fetchPrice);
        //如果执行成功
        future.thenAccept( x -> {
            System.out.println(Thread.currentThread().getName());
        });
        //如果执行失败
        CompletableFuture<Double> exceptionally = future.exceptionally(throwable -> {
            System.out.println("执行失败");
            throwable.printStackTrace();
            return null;
        });

        System.out.println(Thread.currentThread().getName()+ "haha");

        Thread.sleep(20000);
    }
    static Double fetchPrice(){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        if (Math.random() < 0.9){
//            throw new RuntimeException("fetch price fail");
//        }
        return 5 + Math.random()*20;
    }
}
