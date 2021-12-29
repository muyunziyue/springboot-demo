package com.myjava.multithread;

import java.util.concurrent.*;

/**
 * @Author lidexiu
 * @Date 2021/12/15
 * @Description
 */
public class FutureTest {
    public static void main(String[] args) {
        ExecutorService executor = new ThreadPoolExecutor(4, 4, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        Task task = new Task();
        Future<String> future = executor.submit(task);
        String s = null;
        try {
            s = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(s);
        executor.shutdown();
    }


    static class Task implements Callable<String>{
        @Override
        public String call() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Thread.currentThread().getName();
        }
    }
}
