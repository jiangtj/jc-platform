package com.jtj.cloud.common.servlet;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletExceptionAutoConfiguration {

    @Bean
    public BaseExceptionFilter baseExceptionFilter() {
        return new BaseExceptionFilter();
    }

    @Bean
    public JsonResponseContext noViewResponseContext() {
        return new JsonResponseContext();
    }

    @Bean
    public BaseExceptionResolver baseExceptionResolver() {
        return new BaseExceptionResolver();
    }

}
