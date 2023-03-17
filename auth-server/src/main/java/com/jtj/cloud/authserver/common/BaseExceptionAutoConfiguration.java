package com.jtj.cloud.authserver.common;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class BaseExceptionAutoConfiguration {

    @Bean
    public BaseExceptionHandler baseExceptionHandler() {
        return new BaseExceptionHandler();
    }

    @Bean
    public NoViewResponseContext noViewResponseContext() {
        return new NoViewResponseContext();
    }

}
