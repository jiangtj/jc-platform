package com.jiangtj.cloud.auth;

import com.jiangtj.cloud.auth.rbac.RoleProvider;

import java.util.Objects;

public class AuthHolder {
    private static RoleProvider roleProvider;

    public static RoleProvider getRoleProvider() {
        Objects.requireNonNull(roleProvider, "You cannot call getRoleProvider when the application context isn't set up!");
        return roleProvider;
    }

    public static void setRoleProvider(RoleProvider roleProvider) {
        AuthHolder.roleProvider = roleProvider;
    }
}
