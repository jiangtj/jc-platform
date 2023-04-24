package com.jiangtj.cloud.system;

import com.jiangtj.cloud.auth.rbac.Role;
import com.jiangtj.cloud.auth.reactive.AuthReactiveWebFilter;
import com.jiangtj.cloud.auth.reactive.AuthReactorHandler;
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
    public Role systemRole() {
        return RoleInst.SYSTEM;
    }

    @Bean
    public Role systemReadonlyRole() {
        return RoleInst.SYSTEM_READONLY;
    }

    @Bean
    public AuthReactiveWebFilter reactiveLoginFilter() {
        return AuthReactiveWebFilter.builder()
            .exclude("/login")
            .filter(AuthReactorHandler::hasLogin)
            .build();
    }

}
