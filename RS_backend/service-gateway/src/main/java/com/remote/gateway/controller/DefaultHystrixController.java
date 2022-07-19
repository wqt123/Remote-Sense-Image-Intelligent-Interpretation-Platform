package com.remote.gateway.controller;

import com.remote.tools.utils.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultHystrixController {
    @RequestMapping("/defaultfallback")
    public Result<String> defaultFallback(){
        System.out.println("降级操作...");
        return Result.wrapErrorResult("服务不可用") ;
    }
}
