package com.myjava.multithread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author ldx
 * 利用Lock实现一个阻塞队列
 */
public class MyBlockedQueue {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private Queue<String> queue = new LinkedList<>();

    public void addItem(String item){
        lock.lock();
        try {
            System.out.println("addItem get the lock");
            queue.add(item);
            System.out.println("addTime:item is =" + item);
            condition.signalAll();
        }finally {
            lock.unlock();
            System.out.println("addItem release the lock");
        }
    }

    public String getItem() throws InterruptedException {
        lock.lock();
        try {
            System.out.println("getItem get the lock");
            while (queue.isEmpty()){
                condition.await();
            }
            String item = queue.remove();
            System.out.println("getItem:item is =" + item);
            return item;
        }finally {
            lock.unlock();
            System.out.println("getItem release the lock");

        }
    }

}
