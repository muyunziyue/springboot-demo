package com.myjava.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.sql.SQLException;

/**
 * @author tangming
 * @Description jdbc配置
 * @Date 11:08 2019/8/21
 */
@Configuration
@MapperScan(basePackages = {"com.example.demo.mybatis.dao"}, sqlSessionFactoryRef = "mysqlSqlSessionFactory")
public class MySqlDruidDataSource {
    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;

    @Bean(name = "mysqlDataSource")
    @Primary
    public DruidDataSource metadataDruidDataSource() throws SQLException {

        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("system@123");
        druidDataSource.setUrl("jdbc:mysql://write.mysql.goo.com:3306/test_ldx?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true");
        druidDataSource.setFilters("stat,wall");
        druidDataSource.setInitialSize(1);
        druidDataSource.setMinIdle(1);
        druidDataSource.setMaxActive(5);
        druidDataSource.setMaxWait(5000);
        druidDataSource.setUseGlobalDataSourceStat(true);
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");


        return druidDataSource;
    }

    @Bean(name = "mysqlTransactionManager")
    @Primary
    public DataSourceTransactionManager masterTransactionManager(@Qualifier("mysqlDataSource") DruidDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "mysqlSqlSessionFactory")
    @Primary
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("mysqlDataSource") DruidDataSource masterDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(masterDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(mapperLocations));
        return sessionFactory.getObject();
    }

}
