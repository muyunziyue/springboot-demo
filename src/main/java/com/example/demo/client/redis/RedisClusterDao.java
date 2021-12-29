package com.example.demo.client.redis;


import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @Author lidexiu
 */
@Slf4j
public class RedisClusterDao {

    private static String redisConfigPath = "/home/gooagoo/resource/redisCluster.properties";
    private static String ipAndPort;
    /**最小空闲连接数*/
    private static int minIdle = 10;
    /**最大空闲连接数*/
    private static int maxIdle = 50;
    /**最大连接数*/
    private static int maxTotal = 300;
    /**获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1*/
    private static long maxWaitMillis = 10000;//
    /**逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1*/
    private static long timeBetweenEvictionRunsMillis = 30000;
    /**逐出连接的最小空闲时间, 默认1800000毫秒(30分钟)*/
    private static long minEvictableIdleTimeMillis = 30000;
    /**读取超时（毫秒）*/
    private static int timeout = 3000;

    private static JedisCluster jedisCluster = null;

    static {    //读取redis配置
        File file = new File(redisConfigPath);
        if(!file.exists()){
            log.info("/home/gooagoo/resource/redisCluster.properties文件不存在，无法初始化JedisCluster连接!");
        }
        try (InputStream in = new FileInputStream(file)) {
            Properties properties = new Properties();
            properties.load(in);
            ipAndPort = properties.getProperty("ipAndPort");
        } catch (Exception e) {
            log.error("从文件/home/gooagoo/resource/redisCluster.properties读取数据时失败",e);
        }
    }

    private static void init() {
        try {
            //HostAndPort
            if(ipAndPort==null || "".equals(ipAndPort)) {
                return;
            }
            Set<HostAndPort> set = new HashSet<HostAndPort>();
            String[] split = ipAndPort.split(",");
            for (int i = 0; i < split.length; i++) {
                String ipPort = split[i];
                String[] split1 = ipPort.split(":");
                HostAndPort hostAndPort = new HostAndPort(split1[0],Integer.parseInt(split1[1]));
                set.add(hostAndPort);
            }

            //JedisPoolJedis池配置
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMinIdle(minIdle);
            config.setMaxIdle(maxIdle);
            config.setMaxTotal(maxTotal);
            config.setMaxWaitMillis(maxWaitMillis);
            config.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
            config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
            config.setTestOnBorrow(true);
            config.setTestOnReturn(true);
            config.setTestWhileIdle(true);

            jedisCluster = new JedisCluster(set, timeout, config);
        } catch (Exception e) {
            log.error("初始化JedisCluster时出错!",e);
        }
    }

    public synchronized static JedisCluster getJedisCluster() {
        if(jedisCluster == null) {
            init();
        }
        return  jedisCluster;
    }
}
