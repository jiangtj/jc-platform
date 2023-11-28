package com.jiangtj.platform.basereactive;

import com.jiangtj.platform.auth.reactive.AuthReactiveWebFilter;
import com.jiangtj.platform.auth.reactive.AuthReactorHandler;
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
