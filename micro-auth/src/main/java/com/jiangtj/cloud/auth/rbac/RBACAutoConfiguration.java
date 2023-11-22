package com.jiangtj.cloud.auth.rbac;

import com.jiangtj.cloud.auth.context.AuthContextConverter;
import com.jiangtj.cloud.auth.system.DefaultRoleProvider;
import com.jiangtj.cloud.auth.system.Role;
import com.jiangtj.cloud.auth.system.SystemContextConverter;
import com.jiangtj.cloud.auth.system.SystemRoleProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration
public class RBACAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SystemRoleProvider systemRoleProvider(ObjectProvider<List<Role>> op) {
        return new DefaultRoleProvider(op);
    }

    @Bean
    @ConditionalOnMissingBean(name = "systemContextConverter")
    public AuthContextConverter systemContextConverter(SystemRoleProvider systemRoleProvider) {
        return new SystemContextConverter(systemRoleProvider);
    }

}
