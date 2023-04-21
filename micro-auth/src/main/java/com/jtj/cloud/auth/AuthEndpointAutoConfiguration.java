package com.jtj.cloud.auth;

import com.jtj.cloud.auth.rbac.RBACAutoConfiguration;
import com.jtj.cloud.auth.rbac.RoleEndpoint;
import com.jtj.cloud.auth.rbac.RoleProvider;
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

}
