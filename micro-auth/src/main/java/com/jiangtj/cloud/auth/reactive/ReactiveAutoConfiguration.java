package com.jiangtj.cloud.auth.reactive;


import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveAutoConfiguration {

    @Bean
    public ReactiveTokenFilter reactiveTokenFilter() {
        return new ReactiveTokenFilter();
    }

    @Bean
    public ReactiveJWTExceptionHandler reactiveJWTExceptionHandler() {
        return new ReactiveJWTExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthWebClientFilter authWebClientFilter() {
        return new AuthWebClientFilter();
    }

}
