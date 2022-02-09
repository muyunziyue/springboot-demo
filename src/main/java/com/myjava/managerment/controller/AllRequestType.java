package com.myjava.managerment.controller;

import com.myjava.entity.HttpResponse;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ldx
 */
@RestController
@RequestMapping("/request")
public class AllRequestType {

    @GetMapping("/getWithNoArgs")
    public HttpResponse getTypeTest1(){
        return HttpResponse.create();
    }
    @GetMapping("/getWithRequestParam")
    public HttpResponse getTypeWithRequestParam(@RequestParam String requestParam1){
        HttpResponse httpResponse = HttpResponse.create();
        httpResponse.setData(requestParam1);

        return httpResponse;
    }
    @GetMapping("getWithPathVariable/{id}")
    public HttpResponse getTypeWithPathVariable(@PathVariable String id){
        HttpResponse httpResponse = HttpResponse.create();
        httpResponse.setData(id);

        return httpResponse;
    }
}
