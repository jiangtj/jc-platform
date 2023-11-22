package com.jiangtj.cloud.auth.context;

import com.jiangtj.cloud.auth.rbac.RoleProvider;

import java.util.List;

/**
 * Auth 上下文
 */
public interface RoleProviderAuthContext extends AuthContext {

    RoleProvider roleProvider();

    @Override
    default List<String> permissions() {
        return RoleProvider.getPermissionKeys(roleProvider(), roles().toArray(new String[]{}));
    }

    @Override
    default boolean isLogin() {
        return true;
    }

}
