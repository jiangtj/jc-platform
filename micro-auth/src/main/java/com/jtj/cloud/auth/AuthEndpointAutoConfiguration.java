package com.jtj.cloud.auth;

import com.jtj.cloud.auth.rbac.RBACAutoConfiguration;
import com.jtj.cloud.auth.rbac.RoleContext;
import com.jtj.cloud.auth.rbac.RoleEndpoint;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = RBACAutoConfiguration.class)
public class AuthEndpointAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RoleEndpoint roleEndpoint(RoleContext context) {
        return new RoleEndpoint(context);
    }

}
