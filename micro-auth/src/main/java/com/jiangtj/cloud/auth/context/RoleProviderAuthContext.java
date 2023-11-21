package com.jiangtj.cloud.auth.context;

import com.jiangtj.cloud.auth.TokenType;
import com.jiangtj.cloud.auth.rbac.PermissionUtils;
import com.jiangtj.cloud.auth.rbac.RoleProvider;
import io.jsonwebtoken.Claims;

import java.util.List;

/**
 * Auth 上下文
 */
public interface RoleProviderAuthContext extends AuthContext {

    RoleProvider roleProvider();

    @Override
    default List<String> permissions() {
        return PermissionUtils.getPermissionKeys(roleProvider(), roles().toArray(new String[]{}));
    }

    @Override
    default boolean isLogin() {
        return true;
    }

}
