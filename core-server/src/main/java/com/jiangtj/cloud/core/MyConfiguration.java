package com.jiangtj.cloud.core;

import com.jiangtj.cloud.auth.AuthKeyLocator;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MyConfiguration {

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public AuthKeyLocator authKeyLocator() {
        return new CoreAuthKeyLocator();
    }

}
