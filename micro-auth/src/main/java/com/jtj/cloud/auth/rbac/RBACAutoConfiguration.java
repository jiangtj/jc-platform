package com.jtj.cloud.auth.rbac;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

import static com.jtj.cloud.auth.rbac.RBACAutoConfiguration.ORDER;

/**
 * Created At 2021/3/24.
 */
@AutoConfiguration
@AutoConfigureOrder(ORDER)
public class RBACAutoConfiguration {

    public static final int ORDER = 100;

    @Bean
    @ConditionalOnMissingBean
    public RoleContext roleContext(List<Role> roles) {
        return () -> roles;
    }

}
