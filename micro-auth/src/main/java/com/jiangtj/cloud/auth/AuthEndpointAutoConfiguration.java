package com.jiangtj.cloud.auth;

import com.jiangtj.cloud.auth.rbac.RBACAutoConfiguration;
import com.jiangtj.cloud.auth.rbac.RoleEndpoint;
import com.jiangtj.cloud.auth.rbac.RoleProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = RBACAutoConfiguration.class)
public class AuthEndpointAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RoleEndpoint roleEndpoint(RoleProvider roleProvider) {
        return new RoleEndpoint(roleProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public PublicKeyEndpoint publicKeyEndpoint() {
        return new PublicKeyEndpoint();
    }

}
