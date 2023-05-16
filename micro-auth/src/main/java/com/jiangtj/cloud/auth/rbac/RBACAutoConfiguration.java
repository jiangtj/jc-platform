package com.jiangtj.cloud.auth.rbac;

import com.jiangtj.cloud.auth.context.AuthContextConverter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration
public class RBACAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RoleProvider roleProvider(ObjectProvider<List<Role>> op) {
        return new DefaultRoleProvider(op);
    }

    @Bean
    @ConditionalOnMissingBean(name = "roleAuthContextConverter")
    public AuthContextConverter roleAuthContextConverter(RoleProvider roleProvider) {
        return new RoleAuthContextConverter(roleProvider);
    }

}
