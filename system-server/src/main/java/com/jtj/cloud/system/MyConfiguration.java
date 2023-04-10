package com.jtj.cloud.system;

import com.jtj.cloud.auth.rbac.Role;
import com.jtj.cloud.auth.rbac.RoleInst;
import com.jtj.cloud.auth.reactive.ReactiveLoginFilter;
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
    public Role role() {
        return RoleInst.ADMIN.role();
    }

    @Bean
    public ReactiveLoginFilter reactiveLoginFilter() {
        return new ReactiveLoginFilter.builder()
            .without("/","/login")
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
