package com.example.nacos.test;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ldx
 * @date 2022/7/27
 */
@RestController
@RequestMapping("/test")
@Data
public class Test {
//    @Value(value = "${my.test.path}")
    @NacosValue(value = "${my.test.path}", autoRefreshed = true)
    private String key;


    @GetMapping("/key")
    public String getKey(){
        return key;
    }
}
