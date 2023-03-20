package com.jtj.cloud.basereactive;

import com.jtj.cloud.auth.AuthServer;
import jakarta.annotation.Resource;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MyConfiguration {

    @Resource
    AuthServer authServer;

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public AuthWebClientFiler authWebClientFiler() {
        return new AuthWebClientFiler();
    }

}
