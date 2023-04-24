package com.jiangtj.cloud.auth;

import com.jiangtj.cloud.auth.rbac.RoleProvider;
import com.jiangtj.cloud.common.SpringApplicationContextHolder;
import org.springframework.context.ApplicationContext;

import java.util.Objects;

public class AuthHolder {
    private static RoleProvider roleProvider;

    public static RoleProvider getRoleProvider() {
        ApplicationContext context = SpringApplicationContextHolder.getApplicationContext();
        Objects.requireNonNull(context, "You cannot call getRoleProvider when the application context isn't set up!");
        if (roleProvider != null) {
            return roleProvider;
        }
        roleProvider = context.getBean(RoleProvider.class);
        return roleProvider;
    }


}
