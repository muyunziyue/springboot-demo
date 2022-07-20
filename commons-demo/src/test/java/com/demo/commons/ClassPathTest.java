package com.demo.commons;

import com.demo.commons.utils.PropertiesUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author ldx
 * @date 2022/7/19
 */
public class ClassPathTest {

    @Test
    public void classPathTest() throws IOException {
        InputStream is = getClass().getResourceAsStream("/conf.properties");
        Properties properties = new Properties();
        properties.load(is);
        for (String stringPropertyName : properties.stringPropertyNames()) {
            System.out.println(stringPropertyName + ": " + properties.getProperty(stringPropertyName));
        }
    }

    @Test
    public void PropertiesUtilsTest(){
        Properties properties = new Properties();
        PropertiesUtil.loadPropertiesFromClasspath(properties, "/conf.properties");
        for (String stringPropertyName : properties.stringPropertyNames()) {
            System.out.println(stringPropertyName + ": " + properties.getProperty(stringPropertyName));
        }
    }
}
