package com.jtj.cloud.basereactive;

import com.jtj.cloud.common.BaseExceptionUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@EnableDiscoveryClient
public class BaseReactiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseReactiveApplication.class, args);
    }

    @GetMapping("/")
    public String index(){
        return "Base Reactive Client Started !!";
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
