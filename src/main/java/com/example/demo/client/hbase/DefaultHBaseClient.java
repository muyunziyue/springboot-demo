package com.example.demo.client.hbase;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author lidexiu
 */
@Slf4j
public class DefaultHBaseClient {

    private final static ConcurrentMap<String, Connection> CONNECTION_MAP = new ConcurrentHashMap<String, Connection>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                Collection<Connection> values = CONNECTION_MAP.values();
                for (Connection connection : values) {
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (IOException e) {
                            log.error("hbase关闭连接发生异常!cause by=" + e.getMessage(), e);
                            return;
                        }
                    }
                }
                log.info("HBase Client Closed.....");
            }
        }));
    }

    public static Connection getConnection(String tableName) {
        //与HBase数据库的连接对象
        Connection connection = null;
        try {
            connection = CONNECTION_MAP.get(tableName);
            if (connection == null) {
                //取得一个数据库连接的配置参数对象
                Configuration conf = HBaseConfiguration.create();
                //设置连接参数：HBase数据库所在的主机IP
                String quorum = HBaseConfig.getProperty("hbase-bill");
                if (StringUtils.isBlank(quorum)) {
                    log.error("hbase配置文件中" + tableName + ".hbase.zookeeper.quorum取值为空!");
                    return null;
                }
                conf.set("hbase.zookeeper.quorum", quorum);
                conf.set("hbase.rpc.timeout", "5000");

                //取得一个数据库连接对象
                connection = ConnectionFactory.createConnection(conf);
                Connection putIfAbsent = CONNECTION_MAP.putIfAbsent(tableName, connection);
                if (putIfAbsent != null) {
                    connection.close();
                    connection = putIfAbsent;
                }
            }
        } catch (IOException e) {
            log.error("hbase创建连接发生异常!cause by=" + e.getMessage(), e);
            System.exit(1);
        } catch (Throwable e) {
            log.error("hbase创建连接发生异常!cause by=" + e.getMessage(), e);
            System.exit(1);
        }
        return connection;
    }
}
