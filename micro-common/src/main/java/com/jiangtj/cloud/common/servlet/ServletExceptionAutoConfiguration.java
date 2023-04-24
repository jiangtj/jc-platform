package com.jiangtj.cloud.common.servlet;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletExceptionAutoConfiguration {

    @Bean
    public JsonResponseContext noViewResponseContext() {
        return new JsonResponseContext();
    }

    @Bean
    public ServletExceptionHandler servletExceptionHandler() {
        return new ServletExceptionHandler();
    }

    @Bean
    public BaseExceptionFilter baseExceptionFilter() {
        return new BaseExceptionFilter();
    }

    @Bean
    public BaseExceptionResolver baseExceptionResolver() {
        return new BaseExceptionResolver();
    }

}
