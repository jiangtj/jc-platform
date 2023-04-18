package com.jtj.cloud.basereactive;

import com.jtj.cloud.auth.reactive.AuthReactiveWebFilter;
import com.jtj.cloud.auth.reactive.AuthReactorHandler;
import com.jtj.cloud.auth.reactive.AuthWebClientFilter;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MyConfiguration {

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder(AuthWebClientFilter filter) {
        return WebClient.builder().filter(filter);
    }

    @Bean
    public AuthReactiveWebFilter reactiveLoginFilter() {
        return AuthReactiveWebFilter.builder()
            .exclude("/", "/insecure/**")
            .filter(AuthReactorHandler::hasLogin)
            .build();
    }

}
