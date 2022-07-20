package com.example.nacos.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class ClickhouseDruidDataSource {
    private static final Logger LOG = LoggerFactory.getLogger(ClickhouseDruidDataSource.class);
    @Value(value = "${cmdi.datasource.clickhouse.host}")
    private String host;
    @Value(value = "${cmdi.datasource.clickhouse.username}")
    private String username;
    @Value(value = "${cmdi.datasource.clickhouse.password}")
    private String password;
    @Value(value = "${cmdi.datasource.clickhouse.port}")
    private String port;

    public DruidDataSource getDruidDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        try {
            dataSource.setConnectionProperties("socketTimeout=300000;connectTimeout=1200");
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            String[] hosts = host.split(",");
            dataSource.setUrl("jdbc:clickhouse://" + hosts[0] + ":" + port + "?socket_timeout=300000");
            dataSource.setInitialSize(1);
            dataSource.setMinIdle(1);
            dataSource.setMaxActive(32);
            dataSource.setMaxWait(300000);
            dataSource.setUseGlobalDataSourceStat(true);
            dataSource.setDriverClassName("ru.yandex.clickhouse.ClickHouseDriver");
        } catch (Exception e) {
            LOG.error("初始化ClickhouseDataSource失败! cause by={}", e.getMessage(), e);
            System.exit(1);
        }

        return dataSource;
    }
}
