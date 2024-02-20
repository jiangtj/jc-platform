package com.jiangtj.platform.core;

import com.jiangtj.platform.spring.cloud.CoreTokenFilter;
import com.jiangtj.platform.spring.cloud.jwt.AuthKeyLocator;
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

    @Bean
    public CoreTokenFilter coreTokenFilter() {
        return new CoreTokenFilter();
    }

}
