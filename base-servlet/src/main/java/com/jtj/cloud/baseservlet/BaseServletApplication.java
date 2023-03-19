package com.jtj.cloud.baseservlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BaseServletApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseServletApplication.class, args);
    }

}
