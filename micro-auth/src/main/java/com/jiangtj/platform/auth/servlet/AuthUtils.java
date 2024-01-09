package com.jiangtj.platform.auth.servlet;

import com.jiangtj.platform.auth.AuthExceptionUtils;
import com.jiangtj.platform.auth.KeyUtils;
import com.jiangtj.platform.auth.context.AuthContext;

import java.util.List;
import java.util.stream.Stream;

public interface AuthUtils {

    static void isTokenType(Class<?> type) {
        AuthContext ctx = AuthHolder.getAuthContext();
        if (!type.isInstance(ctx)) {
            throw AuthExceptionUtils.unSupport();
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
