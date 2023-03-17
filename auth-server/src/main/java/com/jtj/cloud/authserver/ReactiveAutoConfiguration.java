package com.jtj.cloud.authserver;


import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "auth.filter", matchIfMissing = true)
    public ReactiveTokenFilter reactiveTokenFilter() {
        return new ReactiveTokenFilter();
    }

    @Bean
    public ReactiveJWTExceptionHandler reactiveJWTExceptionHandler() {
        return new ReactiveJWTExceptionHandler();
    }

}
