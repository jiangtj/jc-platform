package com.jtj.cloud.securityresource;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@EnableResourceServer
@EnableFeignClients
@SpringCloudApplication
public class SecurityResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityResourceApplication.class, args);
    }

    @RequestMapping("/")
    public String index() {
        return "Here is a resource server!";
    }

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

}
