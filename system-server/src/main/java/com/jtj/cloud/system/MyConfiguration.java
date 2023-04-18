package com.jtj.cloud.system;

import com.jtj.cloud.auth.rbac.Role;
import com.jtj.cloud.auth.rbac.RoleInst;
import com.jtj.cloud.auth.reactive.AuthReactiveWebFilter;
import com.jtj.cloud.auth.reactive.AuthReactorHandler;
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
        return RoleInst.SYSTEM.permission("system:user:write", "system:user:read", "system:role");
    }

    @Bean
    public Role systemReadonlyRole() {
        return RoleInst.SYSTEM_READONLY.permission("system:user:read");
    }

    @Bean
    public AuthReactiveWebFilter reactiveLoginFilter() {
        return AuthReactiveWebFilter.builder()
            .exclude("/","/login","/role")
            .filter(AuthReactorHandler::hasLogin)
            .build();
    }

    /*@Configuration
    public static class MyWebFluxConfig implements WebFluxConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("*");
        }
    }*/

}
