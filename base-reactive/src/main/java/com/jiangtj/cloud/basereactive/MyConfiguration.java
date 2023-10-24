package com.jiangtj.cloud.basereactive;

import com.jiangtj.cloud.auth.reactive.AuthReactiveWebFilter;
import com.jiangtj.cloud.auth.reactive.AuthReactorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration {

//    @Bean
//    @LoadBalanced
//    public WebClient.Builder loadBalancedWebClientBuilder(AuthWebClientFilter filter) {
//        return WebClient.builder().filter(filter);
//    }

    @Bean
    public AuthReactiveWebFilter reactiveLoginFilter() {
        return AuthReactiveWebFilter.builder()
            .exclude("/", "/insecure/**")
            .filter(AuthReactorHandler::hasLogin)
            .build();
    }

}
