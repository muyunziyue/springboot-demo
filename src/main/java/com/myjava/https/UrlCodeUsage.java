package com.myjava.https;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lidexiu
 * @Date 2021/11/19
 * @Description
 */
@RestController
@RequestMapping("/ldx")
@Slf4j
public class UrlCodeUsage {

    @GetMapping(value = "/url")
    public String getData(@RequestParam String param){
        return param;
    }
}
