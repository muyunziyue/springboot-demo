package com.myjava.multithread;

import java.math.BigDecimal;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author ldx
 * 模拟转账时并发问题
 */
public class MyAccount {
    private final Lock lock = new ReentrantLock();
    private BigDecimal balance = new BigDecimal("0");
    private final Condition condition = lock.newCondition();

    public MyAccount(BigDecimal balance){
        this.balance = balance;
    }
    public MyAccount(){}

    public void transfer(MyAccount account, BigDecimal money) throws InterruptedException {
        while (true){
            Thread.sleep((long)(Math.random()*100));
            if (this.lock.tryLock()){
                try{
                    if (account.lock.tryLock()){
                        try{
                            this.balance = this.balance.subtract(money);
                            account.balance = account.balance.add(money);
                        }finally {
                            account.lock.unlock();
                        }
                    }
                }finally {
                    this.lock.unlock();
                }
            }
        }
    }

    public BigDecimal checkMoney(){
        this.lock.lock();
        try{
            return this.balance;
        }finally {
            this.lock.unlock();
        }
    }
}
