package com.jiangtj.cloud.auth;

import com.jiangtj.cloud.auth.context.AuthContextConverter;
import com.jiangtj.cloud.auth.context.AuthContextFactory;
import com.jiangtj.cloud.auth.context.ServerContextImpl;
import com.jiangtj.cloud.auth.context.SystemUserContextImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthAutoConfiguration {

    @Bean
    public AuthServer authServer() {
        return new AuthServer();
    }

    @Bean
    @ConditionalOnMissingBean(name = "authSystemUserContextConverter")
    public AuthContextConverter authSystemUserContextConverter() {
        return AuthContextConverter.register(TokenType.SYSTEM_USER, SystemUserContextImpl::new);
    }

    @Bean
    @ConditionalOnMissingBean(name = "authServerContextConverter")
    public AuthContextConverter authServerContextConverter() {
        return AuthContextConverter.register(TokenType.SERVER, ServerContextImpl::new);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthContextFactory authContextFactory(AuthServer authServer, List<AuthContextConverter> converters) {
        return new AuthContextFactory(authServer, converters);
    }

}
