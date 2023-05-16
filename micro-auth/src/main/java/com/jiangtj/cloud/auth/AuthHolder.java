package com.jiangtj.cloud.auth;

import com.jiangtj.cloud.auth.rbac.RoleProvider;

import java.util.Objects;

public class AuthHolder {
    private static RoleProvider defaultToleProvider;

    public static RoleProvider getDefaultRoleProvider() {
        Objects.requireNonNull(defaultToleProvider, "You cannot call getRoleProvider when the application context isn't set up!");
        return defaultToleProvider;
    }

    public static void setDefaultRoleProvider(RoleProvider roleProvider) {
        AuthHolder.defaultToleProvider = roleProvider;
    }
}
