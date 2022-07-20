package com.example.nacos;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@NacosPropertySource(dataId = "example.properties", autoRefreshed = true)
public class NacosApplication {

	public static void main(String[] args) {
		SpringApplication.run(NacosApplication.class, args);
	}

}
