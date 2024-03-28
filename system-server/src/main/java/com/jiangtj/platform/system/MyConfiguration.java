package com.jiangtj.platform.system;

import com.jiangtj.platform.auth.servlet.ServletLoginFilter;
import com.jiangtj.platform.spring.cloud.client.ClientMutator;
import com.jiangtj.platform.spring.cloud.system.Role;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

@Configuration
public class MyConfiguration {

    @Bean
    public Role systemRole() {
        return RoleInst.SYSTEM;
    }

    @Bean
    public Role systemReadonlyRole() {
        return RoleInst.SYSTEM_READONLY;
    }

    @Bean
    @Primary
    @LoadBalanced
    @ClientMutator
    public RestClient.Builder restClient() {
        return RestClient.builder();
    }

    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancedClient() {
        return RestClient.builder();
    }

    @Bean
    public ServletLoginFilter servletLoginFilter() {
        return new ServletLoginFilter.builder()
            .without("/login")
            .build();
    }

    /*@Bean
    public AuthReactiveWebFilter reactiveLoginFilter() {
        return AuthReactiveWebFilter.builder()
            .exclude("/login")
            .filter(AuthReactorHandler::hasLogin)
            .build();
    }*/

    /*@Bean
    public DSLContext dslContext(ConnectionFactory connectionFactory) {
        return DSL.using(connectionFactory);
    }*/

}
