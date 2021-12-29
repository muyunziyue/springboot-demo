package com.example.demo.client.redis;

/**
 * @Author lidexiu
 */
public class Main {
    public static void main(String[] args) {
        RedisClient client = RedisClientManager.getManager().getClient();
        String s = client.get("1FO222NIB8LR180CJM9BO2UNST54GHFH");
        System.out.println(s);
    }
}
