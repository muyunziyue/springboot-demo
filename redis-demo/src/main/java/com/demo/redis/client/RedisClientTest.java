package com.demo.redis.client;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/**
 * @author ldx
 * @date 2022/9/28
 */
public class RedisClientTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
    testAsyncClient();

    }

    public static void testSyncClient(){
        RedisURI redisURI = RedisURI.builder()
                .withHost("192.168.1.64")
                .withPort(6379)
                .withPassword("123456".toCharArray())
                .build();

        RedisClient redisClient = RedisClient.create(redisURI);
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        RedisCommands<String, String> syncCommands = connect.sync();
        syncCommands.set("lettuce-key", "Hello Lettuce");

        connect.close();
        redisClient.shutdown();
    }

    public static void testAsyncClient() throws ExecutionException, InterruptedException {
        RedisURI redisURI = RedisURI.builder()
                .withHost("192.168.1.64")
                .withPort(6379)
                .withPassword("123456".toCharArray())
                .build();
        RedisClient redisClient = RedisClient.create(redisURI);
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        RedisAsyncCommands<String, String> asyncCommands = connect.async();
        RedisFuture<String> future = asyncCommands.get("lettuce-key");

        // 直接获取，会阻塞,也可以只设置一个超时时间:get(long timeout, TimeUnit unit)
        String value = future.get();
        System.out.println("block call, value = " + value);
        // push方式消费,也可使用Lambda表达式
        future.thenAcceptAsync(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println("push consume, value = " + s);
            }
        });


    }
}
