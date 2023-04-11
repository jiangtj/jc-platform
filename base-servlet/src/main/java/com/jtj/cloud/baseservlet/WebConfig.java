package com.jtj.cloud.baseservlet;

import com.jtj.cloud.auth.servlet.ServletLoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("*");
    }

    @Bean
    public ServletLoginFilter servletLoginFilter() {
        return new ServletLoginFilter.builder()
            .without("/", "/insecure/**")
            .build();
    }
}
