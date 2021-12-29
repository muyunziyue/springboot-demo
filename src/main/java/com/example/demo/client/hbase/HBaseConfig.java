package com.example.demo.client.hbase;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.example.demo.client.hbase.HBaseConstants.HBASE_ZOOKEEPER_QUORUM;

/**
 * @Author lidexiu
 */
@Slf4j
public class HBaseConfig {

    public static String getProperty(String tableName) {
        Properties props = new Properties();
        String property = null;
        try (InputStream in = new FileInputStream("/home/gooagoo/resource/" + tableName + ".properties")) {
            props.load(in);
            property = props.getProperty(HBASE_ZOOKEEPER_QUORUM);
            if (StringUtils.isNotBlank(property)) {
                property = property.trim();
            }
        } catch (FileNotFoundException e) {
            log.error("HBase配置文件不存在!cause by=" + e.getMessage(), e);
        } catch (IOException e) {
            log.error("HBase配置文件加载发生异常!cause by=" + e.getMessage(), e);
        } catch (Throwable e) {
            log.error("HBase配置文件加载发生异常!cause by=" + e.getMessage(), e);
        }
        return property;
    }

}
