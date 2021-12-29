package com.myjava.multithread;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * @Author lidexiu
 * @Date 2021/12/15
 * @Description
 */
public class CompletableFutureTest2 {
    public static void main(String[] args) throws InterruptedException {
        CompletableFuture<String> from_sina = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("from sina");
                return "1001";
            }
        });

        CompletableFuture<String> from_163 = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("from 163");
                return "1002";
            }
        });
        CompletableFuture<Object> cfFuture = CompletableFuture.anyOf(from_sina, from_163);

        CompletableFuture<String> from_sina2 = cfFuture.thenApplyAsync(code -> (String) code + " sina");
        CompletableFuture<String> from_1632 = cfFuture.thenApplyAsync(code -> (String) code + " 163");

        CompletableFuture<Object> cfQuery = CompletableFuture.anyOf(from_sina2, from_1632);
        cfQuery.thenAccept(System.out::println);

        Thread.sleep(10000);
    }


}
