package com.jiangtj.platform.spring.cloud;

import com.jiangtj.platform.spring.cloud.client.DefaultTokenMutator;
import com.jiangtj.platform.spring.cloud.client.TokenMutateService;
import com.jiangtj.platform.spring.cloud.client.TokenMutator;
import com.jiangtj.platform.spring.cloud.jwt.JwtAuthContextFactory;
import com.jiangtj.platform.spring.cloud.jwt.JwtAuthContextProvider;
import com.jiangtj.platform.spring.cloud.jwt.MicroAuthContextConverter;
import com.jiangtj.platform.spring.cloud.server.ServerContextImpl;
import com.jiangtj.platform.spring.cloud.server.ServerProviderProperties;
import com.jiangtj.platform.spring.cloud.system.DefaultSystemRoleProvider;
import com.jiangtj.platform.spring.cloud.system.Role;
import com.jiangtj.platform.spring.cloud.system.SystemContextProvider;
import com.jiangtj.platform.spring.cloud.system.SystemRoleProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration
@EnableConfigurationProperties({
        AuthProperties.class,
        ServerProviderProperties.class
})
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
    public JwtAuthContextFactory jwtAuthContextFactory(AuthServer authServer, List<JwtAuthContextProvider> converters) {
        return new JwtAuthContextFactory(authServer, converters);
    }

    @Bean
    public PublicKeyCachedService publicKeyCachedService() {
        return new PublicKeyCachedService();
    }

    @Bean
    @ConditionalOnMissingBean(name = "serverContextProvider")
    public JwtAuthContextProvider serverContextProvider() {
        return JwtAuthContextProvider.create(Providers.SERVER, ServerContextImpl::new);
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenMutateService tokenMutateService(ObjectProvider<TokenMutator> mutators) {
        return new TokenMutateService(mutators);
    }

    @Bean
    public DefaultTokenMutator defaultTokenMutator() {
        return new DefaultTokenMutator();
    }

    @Bean
    @ConditionalOnMissingBean
    public SystemRoleProvider systemRoleProvider(ObjectProvider<List<Role>> op) {
        return new DefaultSystemRoleProvider(op);
    }

    @Bean
    @ConditionalOnMissingBean(name = "systemContextConverter")
    public JwtAuthContextProvider systemContextConverter(SystemRoleProvider systemRoleProvider) {
        return new SystemContextProvider(systemRoleProvider);
    }
}
