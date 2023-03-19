package com.jtj.cloud.baseservlet;

import com.jtj.cloud.common.BaseExceptionUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class BaseController {

    @GetMapping("/")
    public String index(){
        return "Base Servlet Client Started !!";
    }

    @GetMapping("/insecure/err")
    public String err(){
        throw BaseExceptionUtils.invalidToken("insecure");
    }

    @GetMapping("/needtoken")
    public String needToken(){
        return "这个请求需要token";
    }
}
