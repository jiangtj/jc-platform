package com.jiangtj.platform.auth.cloud;

import com.jiangtj.platform.auth.AuthAutoConfiguration;
import com.jiangtj.platform.auth.cloud.system.RoleEndpoint;
import com.jiangtj.platform.auth.cloud.system.SystemRoleProvider;
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
