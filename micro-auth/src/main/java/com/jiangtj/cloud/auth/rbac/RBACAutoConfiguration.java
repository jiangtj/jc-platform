package com.jiangtj.cloud.auth.rbac;

import com.jiangtj.cloud.auth.AuthHolder;
import org.springframework.beans.factory.ObjectProvider;
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
    public RoleProvider roleProvider(ObjectProvider<List<Role>> op) {
        DefaultRoleProvider provider = new DefaultRoleProvider(op);
        AuthHolder.setRoleProvider(provider);
        return provider;
    }

}
