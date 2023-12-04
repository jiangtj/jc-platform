package com.jiangtj.platform.auth.cloud;

import com.jiangtj.platform.auth.TokenMutateService;
import com.jiangtj.platform.auth.TokenMutator;
import com.jiangtj.platform.auth.TokenType;
import com.jiangtj.platform.auth.cloud.server.ServerContextImpl;
import com.jiangtj.platform.auth.cloud.system.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthCloudAutoConfiguration {

    @Bean
    public AuthServer authServer() {
        return new AuthServer();
    }

    @Bean
    public MicroAuthContextConverter microAuthContextConverter() {
        return new MicroAuthContextConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthContextFactory jwtAuthContextFactory(AuthServer authServer, List<JwtAuthContextConverter> converters) {
        return new JwtAuthContextFactory(authServer, converters);
    }

    @Bean
    public PublicKeyCachedService publicKeyCachedService() {
        return new PublicKeyCachedService();
    }

    @Bean
    public CoreInstanceService coreInstanceService() {
        return new CoreInstanceService();
    }

    @Bean
    @ConditionalOnMissingBean(name = "serverContextConverter")
    public JwtAuthContextConverter serverContextConverter() {
        return JwtAuthContextConverter.register(TokenType.SERVER, ServerContextImpl::new);
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

    @Bean
    @ConditionalOnMissingBean
    public SystemRoleProvider systemRoleProvider(ObjectProvider<List<Role>> op) {
        return new DefaultSystemRoleProvider(op);
    }

    @Bean
    @ConditionalOnMissingBean(name = "systemContextConverter")
    public JwtAuthContextConverter systemContextConverter(SystemRoleProvider systemRoleProvider) {
        return new SystemContextConverter(systemRoleProvider);
    }
}
