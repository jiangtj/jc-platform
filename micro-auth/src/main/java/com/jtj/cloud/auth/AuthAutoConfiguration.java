package com.jtj.cloud.auth;

import com.jtj.cloud.auth.context.AuthContextConverter;
import com.jtj.cloud.auth.context.AuthContextFactory;
import com.jtj.cloud.auth.context.ServerContextImpl;
import com.jtj.cloud.auth.context.SystemUserContextImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AutoConfiguration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthAutoConfiguration {

    @Bean
    public AuthServer authServer() {
        return new AuthServer();
    }

    @Bean
    public AuthHolder authHolder() {
        return new AuthHolder();
    }

    @Bean
    @ConditionalOnMissingBean(name = "authSystemUserContextConverter")
    public AuthContextConverter authSystemUserContextConverter() {
        return AuthContextConverter.register(TokenType.SYSTEM_USER, (token, body) -> {
            String subject = body.getSubject();
            List<String> roleList = Optional.ofNullable(body.get("role", String.class))
                .map(r -> r.split(","))
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
            return new SystemUserContextImpl(new UserClaims(subject, roleList), token, body, Collections.emptyMap());
        });
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
