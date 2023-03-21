package com.jtj.cloud.common.servlet;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletExceptionAutoConfiguration {

    @Bean
    public ServletExceptionHandlerAdvice servletExceptionHandlerAdvice() {
        return new ServletExceptionHandlerAdvice();
    }

    @Bean
    public BaseExceptionFilter baseExceptionFilter() {
        return new BaseExceptionFilter();
    }

    @Bean
    public NoViewResponseContext noViewResponseContext() {
        return new NoViewResponseContext();
    }

    @Bean
    public BaseExceptionResolver baseExceptionResolver() {
        return new BaseExceptionResolver();
    }

//    @Bean
//    public ServletProblemDetailsExceptionHandler servletProblemDetailsExceptionHandler() {
//        return new ServletProblemDetailsExceptionHandler();
//    }

}
