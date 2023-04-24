package com.jiangtj.cloud.baseservlet;

import com.jiangtj.cloud.common.BaseExceptionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @GetMapping("/")
    public String index(){
        return "Base Servlet Client Started !!";
    }

    @GetMapping("/insecure/err")
    public String err(){
        throw BaseExceptionUtils.badRequest("insecure");
    }

    @GetMapping("/insecure/err2")
    public String err2() {
        throw new RuntimeException("系统错误");
    }

    @GetMapping("/needtoken")
    public String needToken(){
        return "这个请求需要token";
    }
}
