package com.jiangtj.platform.auth;

import com.jiangtj.platform.auth.context.AuthContextConverter;
import com.jiangtj.platform.auth.context.AuthContextFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class AuthAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AuthContextFactory authContextFactory(ObjectProvider<AuthContextConverter> converters) {
        return new AuthContextFactory(converters);
    }

}
