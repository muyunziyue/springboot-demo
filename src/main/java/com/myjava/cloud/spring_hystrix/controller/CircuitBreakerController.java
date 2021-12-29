package com.myjava.cloud.spring_hystrix.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author lidexiu
 * @Date 2021/12/17
 * @Description
 */
@Slf4j
@RestController
@RequestMapping("/circuit")
public class CircuitBreakerController {
    private AtomicInteger atomicInteger  = new AtomicInteger(0);

    @GetMapping("/open")
    @HystrixCommand(
            fallbackMethod = "openFallback",
            commandProperties = {
                    @HystrixProperty(name="metrics.rollingStats.timeInMilliseconds", value = "5000"),
                    @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="4"),
                    @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="50"),
                    @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="3000")
            }
    )
    public String open(){
        log.info("c={}", atomicInteger.get());
        int i = atomicInteger.incrementAndGet();
        if (i >2){
            throw new RuntimeException();
        }else {
            return "i=" + i;
        }

    }

    public String openFallback(Throwable t){
        return "fallback: " + atomicInteger.get() + "," + t.getClass() + "=" + t.getMessage();
    }
}
