package com.jiangtj.platform.system;

import com.jiangtj.platform.auth.reactive.AuthReactiveWebFilter;
import com.jiangtj.platform.auth.reactive.AuthReactorHandler;
import com.jiangtj.platform.spring.cloud.client.ClientMutator;
import com.jiangtj.platform.spring.cloud.system.Role;
import io.r2dbc.spi.ConnectionFactory;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

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
    @LoadBalanced
    @ClientMutator
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public AuthReactiveWebFilter reactiveLoginFilter() {
        return AuthReactiveWebFilter.builder()
            .exclude("/login")
            .filter(AuthReactorHandler::hasLogin)
            .build();
    }

    @Bean
    public DSLContext dslContext(ConnectionFactory connectionFactory) {
        return DSL.using(connectionFactory);
    }

}
