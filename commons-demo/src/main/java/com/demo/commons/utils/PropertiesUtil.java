package com.demo.commons.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author ldx
 * @date 2022/7/19
 */

public class PropertiesUtil {

    /**
     * 加载classPath中的配置文件
     * @param properties
     * @param fileName
     */
    public static void loadPropertiesFromClasspath(Properties properties, String fileName) {

        Class<PropertiesUtil> clazz = PropertiesUtil.class;
        try (InputStream inputStream = clazz.getResourceAsStream(fileName)) {
            if (inputStream == null) {
                String msg = "no " + fileName + " configuration file available in the classpath";
            } else {
                properties.load(inputStream);
            }
        } catch (Exception e) {
            //
        }
    }

    /**
     * 根据绝对路径加载配置文件
     * @param properties
     * @param absolutePath
     */
    public static void loadPropertiesByAbsolutePath(Properties properties, String absolutePath) {
        try (InputStream inputStream = new FileInputStream(absolutePath)) {
            properties.load(inputStream);
        } catch (Exception e) {
            //
        }
    }
}
