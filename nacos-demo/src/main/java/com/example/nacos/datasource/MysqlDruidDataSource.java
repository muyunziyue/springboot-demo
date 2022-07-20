package com.example.nacos.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
@Data
public class MysqlDruidDataSource {
    private static final Logger LOG = LoggerFactory.getLogger(MysqlDruidDataSource.class);
    @Value("${cmdi.datasource.mysql.username}")
    private String username;
    @Value("${cmdi.datasource.mysql.password}")
    private String password;
    @Value("${cmdi.datasource.mysql.url}")
    private String url;

    public DruidDataSource getDruidDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        try {
            dataSource.setName("DataSource-MySQL");
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            dataSource.setUrl(url);
            dataSource.setFilters("stat,wall");
            dataSource.setInitialSize(1);
            dataSource.setMinIdle(1);
            dataSource.setMaxActive(3);
            dataSource.setMaxWait(5000);
            dataSource.setUseGlobalDataSourceStat(true);
        } catch (SQLException e) {
            LOG.error("初始化MysqlDataSource失败!cause by={}", e.getMessage(), e);
            System.exit(1);
        }

        return dataSource;

    }


}
