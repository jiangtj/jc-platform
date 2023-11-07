package com.jiangtj.cloud.auth;

import com.jiangtj.cloud.auth.context.AuthContextConverter;
import com.jiangtj.cloud.auth.context.AuthContextFactory;
import com.jiangtj.cloud.auth.context.ServerContextImpl;
import org.springframework.beans.factory.ObjectProvider;
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
    public CoreInstanceService coreInstanceService() {
        return new CoreInstanceService();
    }

    @Bean
    public PublicKeyCachedService publicKeyCachedService() {
        return new PublicKeyCachedService();
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

    @Bean
    @ConditionalOnMissingBean
    public TokenMutateService tokenMutateService(ObjectProvider<TokenMutator> mutators) {
        return new TokenMutateService(mutators);
    }

    @Bean
    @ConditionalOnMissingBean(name = "systemUserTokenMutator")
    public SystemUserTokenMutator systemUserTokenMutator() {
        return new SystemUserTokenMutator();
    }

}
