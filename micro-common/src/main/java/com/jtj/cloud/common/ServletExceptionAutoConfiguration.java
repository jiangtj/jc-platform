package com.jtj.cloud.common;

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

}
