package com.example.demo;

import com.myjava.managerment.service.ApplicationContextServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    ApplicationContextServiceTest applicationContextServiceTest;

    @Test
    void contextLoads() {
        applicationContextServiceTest.getBeans();
    }

}
