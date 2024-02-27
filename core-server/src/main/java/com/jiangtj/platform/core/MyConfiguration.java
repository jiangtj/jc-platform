package com.jiangtj.platform.core;

import com.jiangtj.platform.spring.cloud.jwt.AuthKeyLocator;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class MyConfiguration {

    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancedWebClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public AuthKeyLocator authKeyLocator() {
        return new CoreAuthKeyLocator();
    }

    @Bean
    public CoreContextConverter coreContextConverter() {
        return new CoreContextConverter();
    }

}
