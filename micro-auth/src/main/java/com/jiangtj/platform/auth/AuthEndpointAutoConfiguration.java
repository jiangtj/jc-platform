package com.jiangtj.platform.auth;

import com.jiangtj.platform.auth.system.RoleEndpoint;
import com.jiangtj.platform.auth.system.SystemRoleProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = AuthAutoConfiguration.class)
public class AuthEndpointAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RoleEndpoint roleEndpoint(SystemRoleProvider systemRoleProvider) {
        return new RoleEndpoint(systemRoleProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public PublicKeyEndpoint publicKeyEndpoint() {
        return new PublicKeyEndpoint();
    }

}
