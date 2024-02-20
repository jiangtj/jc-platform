package com.jiangtj.platform.spring.boot.reactive;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveExceptionAutoConfiguration {

    @Bean
    public BaseExceptionHandler baseExceptionHandler() {
        return new BaseExceptionHandler();
    }

    @Bean
    public NoViewResponseContext noViewResponseContext() {
        return new NoViewResponseContext();
    }

    @Bean
    public FixServerWebExchangeContext fixServerWebExchangeContext() {
        return new FixServerWebExchangeContext();
    }

}
