package com.example.demo.client.redis;

import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.Set;

public interface RedisClient {

    Long setnx(String key, String value) throws JedisException;

    Long expire(String key, int seconds) throws JedisException;

    String get(String key) throws JedisException;

    String getSet(String key, String value) throws JedisException;

    Long del(String key) throws JedisException;

    Long del(String... keys) throws JedisException;

    String setex(String key, int seconds, String value) throws JedisException;

    Set<String> smembers(String key) throws JedisException;

    Long sadd(String key, String... members) throws JedisException;

    Object eval(String script, List<String> keys, List<String> args) throws JedisException;

    Long incr(String key) throws JedisException;
}
