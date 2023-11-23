package com.jiangtj.cloud.auth.servlet;

import com.jiangtj.cloud.auth.AuthExceptionUtils;
import com.jiangtj.cloud.auth.KeyUtils;
import com.jiangtj.cloud.auth.context.AuthContext;
import com.jiangtj.cloud.common.BaseExceptionUtils;

import java.util.List;
import java.util.stream.Stream;

public interface AuthUtils {

    static void isTokenType(String type) {
        AuthContext ctx = AuthHolder.getAuthContext();
        if (!type.equals(ctx.type())) {
            throw BaseExceptionUtils.forbidden("不允许访问 todo");
        }
    }

    static void hasLogin() {
        AuthContext ctx = AuthHolder.getAuthContext();
        if (!ctx.isLogin()) {
            throw AuthExceptionUtils.unLogin();
        }
    }

    static void hasRole(String... roles) {
        AuthContext ctx = AuthHolder.getAuthContext();
        List<String> userRoles = ctx.roles();
        Stream.of(roles)
                .map(KeyUtils::toKey)
                .forEach(role -> {
                    if (!userRoles.contains(role)) {
                        throw AuthExceptionUtils.noRole(role);
                    }
                });
    }

    static void hasPermission(String... permissions) {
        AuthContext ctx = AuthHolder.getAuthContext();
        List<String> userPermissions = ctx.permissions();
        Stream.of(permissions)
                .forEach(perm -> {
                    if (!userPermissions.contains(perm)) {
                        throw AuthExceptionUtils.noPermission(perm);
                    }
                });
    }

}
