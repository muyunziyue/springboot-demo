package com.example.demo.client.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @Author lidexiu
 * Redis客户端
 */
@Slf4j
public class RedisClientManager {

    private static String redisConfigPath = "/home/gooagoo/resource/redisCluster.properties";
    private static final boolean SINGLE_MODE = false;

    /**
     * 最大空闲连接数
     */
    private static int single_maxIdle = 200;
    /**
     * 最大连接数
     */
    private static int single_maxTotal = 300;
    /**
     * 读取超时（毫秒）
     */
    private static int timeout = 3000;

    private static JedisCluster jedisCluster = null;

    private static JedisPool jedisPool = null;

    private static RedisClientManager instance = new RedisClientManager();

    private RedisClientManager() {
    }

    public static RedisClientManager getManager() {
        return instance;
    }

    static {
        //读取redis配置
        File file = new File(redisConfigPath);
        if (!file.exists()) {
            log.error("/home/gooagoo/resource/redisCluster.properties文件不存在，无法初始化Jedis连接!");
            System.exit(1);
        }
        try (InputStream in = new FileInputStream(file)) {
            Properties properties = new Properties();
            properties.load(in);
            String ipAndPort = properties.getProperty("ipAndPort");
            if (StringUtils.hasText(ipAndPort)) {
                init(ipAndPort);
            } else {
                log.error("从文件/home/gooagoo/resource/redisCluster.properties读取ipAndPort为空!");
                System.exit(1);
            }
        } catch (Exception e) {
            log.error("从文件/home/gooagoo/resource/redisCluster.properties读取数据时失败!", e);
            System.exit(1);
        }
    }

    private static void init(String ipAndPort) {

        try {
            if (SINGLE_MODE) {
                String[] ipPorts = ipAndPort.split(",");
                jedisPool = initSingleRedis(ipPorts[0]);
            } else {
                jedisCluster = initRedisCluster();
            }
        } catch (Exception e) {
            log.error("初始化Redis客户端时出错! ipAndPort=" + ipAndPort, e);
            System.exit(1);
        }
    }

    private static JedisPool initSingleRedis(String ipPort) {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(single_maxIdle);
        config.setMaxTotal(single_maxTotal);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);
        String[] split = ipPort.split(":");
        return new JedisPool(config, split[0], Integer.parseInt(split[1]), timeout);
    }

    private static JedisCluster initRedisCluster() {
        return RedisClusterDao.getJedisCluster();
    }

    /**
     * 根据实际部署环境获取客户端
     *
     * @return
     */
    public RedisClient getClient() {
        if (SINGLE_MODE) {
            return new SingleRedisClient();
        } else {
            return new ClusterRedisClient();
        }
    }

    /**
     * 私有云单机环境下Redis客户端
     */
    private class SingleRedisClient implements RedisClient {

        @Override
        public Long setnx(String key, String value) throws JedisException {
            Jedis jedis = null;
            Long r = null;
            try {
                jedis = jedisPool.getResource();
                r = jedis.setnx(key, value);
            } catch (Exception e) {
                throw new JedisException(e);
            } finally {
                releaseConnection(jedis);
            }
            return r;
        }

        @Override
        public Long expire(String key, int seconds) throws JedisException {
            Jedis jedis = null;
            Long r = null;
            try {
                jedis = jedisPool.getResource();
                r = jedis.expire(key, seconds);
            } catch (Exception e) {
                throw new JedisException(e);
            } finally {
                releaseConnection(jedis);
            }
            return r;
        }

        @Override
        public String get(String key) throws JedisException {
            Jedis jedis = null;
            String r = null;
            try {
                jedis = jedisPool.getResource();
                r = jedis.get(key);
            } catch (Exception e) {
                throw new JedisException(e);
            } finally {
                releaseConnection(jedis);
            }
            return r;
        }

        @Override
        public String getSet(String key, String value) throws JedisException {
            Jedis jedis = null;
            String r = null;
            try {
                jedis = jedisPool.getResource();
                r = jedis.getSet(key, value);
            } catch (Exception e) {
                throw new JedisException(e);
            } finally {
                releaseConnection(jedis);
            }
            return r;
        }

        @Override
        public Long del(String key) throws JedisException {
            Jedis jedis = null;
            Long r = null;
            try {
                jedis = jedisPool.getResource();
                r = jedis.del(key);
            } catch (Exception e) {
                throw new JedisException(e);
            } finally {
                releaseConnection(jedis);
            }
            return r;
        }

        @Override
        public Long del(String... keys) throws JedisException {
            Jedis jedis = null;
            Long r = null;
            try {
                jedis = jedisPool.getResource();
                r = jedis.del(keys);
            } catch (Exception e) {
                throw new JedisException(e);
            } finally {
                releaseConnection(jedis);
            }
            return r;
        }

        @Override
        public String setex(String key, int seconds, String value) throws JedisException {
            Jedis jedis = null;
            String r = null;
            try {
                jedis = jedisPool.getResource();
                r = jedis.setex(key, seconds, value);
            } catch (Exception e) {
                throw new JedisException(e);
            } finally {
                releaseConnection(jedis);
            }
            return r;
        }

        @Override
        public Set<String> smembers(String key) throws JedisException {
            Jedis jedis = null;
            Set<String> r = null;
            try {
                jedis = jedisPool.getResource();
                r = jedis.smembers(key);
            } catch (Exception e) {
                throw new JedisException(e);
            } finally {
                releaseConnection(jedis);
            }
            return r;
        }

        @Override
        public Long sadd(String key, String... members) throws JedisException {
            Jedis jedis = null;
            Long r = null;
            try {
                jedis = jedisPool.getResource();
                r = jedis.sadd(key, members);
            } catch (Exception e) {
                throw new JedisException(e);
            } finally {
                releaseConnection(jedis);
            }
            return r;
        }

        @Override
        public Object eval(String script, List<String> keys, List<String> args) throws JedisException {
            Jedis jedis = null;
            Object r = null;
            try {
                jedis = jedisPool.getResource();
                r = jedis.eval(script, keys, args);
            } catch (Exception e) {
                throw new JedisException(e);
            } finally {
                releaseConnection(jedis);
            }
            return r;
        }

        @Override
        public Long incr(String key) throws JedisException {
            return null;
        }

        private void releaseConnection(Jedis connection) {
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * 公有云集群模式下Redis客户端
     */
    private class ClusterRedisClient implements RedisClient {

        @Override
        public Long setnx(String key, String value) throws JedisException {
            return jedisCluster.setnx(key, value);
        }

        @Override
        public Long expire(String key, int seconds) throws JedisException {
            return jedisCluster.expire(key, seconds);
        }

        @Override
        public String get(String key) throws JedisException {
            return jedisCluster.get(key);
        }

        @Override
        public String getSet(String key, String value) throws JedisException {
            return jedisCluster.getSet(key, value);
        }

        @Override
        public Long del(String key) throws JedisException {
            return jedisCluster.del(key);
        }

        @Override
        public Long del(String... keys) throws JedisException {
            return jedisCluster.del(keys);
        }

        @Override
        public String setex(String key, int seconds, String value) throws JedisException {
            return jedisCluster.setex(key, seconds, value);
        }

        @Override
        public Set<String> smembers(String key) throws JedisException {
            return jedisCluster.smembers(key);
        }

        @Override
        public Long sadd(String key, String... members) throws JedisException {
            return jedisCluster.sadd(key, members);
        }

        @Override
        public Object eval(String script, List<String> keys, List<String> args) throws JedisException {
            return jedisCluster.eval(script, keys, args);
        }

        @Override
        public Long incr(String key) throws JedisException {
            return jedisCluster.incr(key);
        }
    }

}