package com.jtj.cloud.auth.rbac;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import java.util.List;

@AutoConfiguration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
public class RBACAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RoleContext roleContext(List<Role> roles) {
        return () -> roles;
    }

}
