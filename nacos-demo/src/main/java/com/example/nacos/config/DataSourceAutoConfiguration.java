package com.example.nacos.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.nacos.datasource.ClickhouseDruidDataSource;
import com.example.nacos.datasource.MysqlDruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceAutoConfiguration {
    @Bean(name = "clickhouseDataSource")
    public DruidDataSource clickhouseDataSource(@Qualifier("clickhouseDruidDataSource") ClickhouseDruidDataSource clickhouseDruidDataSource) {
        return clickhouseDruidDataSource.getDruidDataSource();
    }
    @Bean(name = "mysqlDataSource")
    public DruidDataSource mysqlDataSource(@Qualifier("mysqlDruidDataSource") MysqlDruidDataSource mysqlDruidDataSource) {
        return mysqlDruidDataSource.getDruidDataSource();
    }
}
