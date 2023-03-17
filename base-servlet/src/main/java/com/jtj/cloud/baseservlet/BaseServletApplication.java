package com.jtj.cloud.baseservlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@EnableDiscoveryClient
public class BaseServletApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseServletApplication.class, args);
    }

    @GetMapping("/")
    public String index(){
        return "Base Servlet Client Started !!";
    }
}
