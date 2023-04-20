package com.jtj.cloud.auth.context;

import com.jtj.cloud.auth.AuthHolder;
import com.jtj.cloud.auth.UserClaims;
import com.jtj.cloud.auth.rbac.Permission;
import com.jtj.cloud.auth.rbac.Role;
import io.jsonwebtoken.Claims;

import java.util.Collection;
import java.util.List;

/**
 * Auth 上下文
 */
public interface AuthContext {

    boolean isLogin();

    UserClaims user();

    String token();

    Claims claims();

    default List<String> roles() {
        return user().roles();
    }

    default List<String> permissions() {
        return roles().stream()
            .flatMap(roleKey -> AuthHolder.getRoleContext().getRoles().stream()
                .filter(role -> role.key().equals(roleKey))
                .map(Role::permissions)
                .flatMap(Collection::stream))
            .map(Permission::toString)
            .toList();
    }

    default AuthContext put(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    default Object get(String key) {
        throw new UnsupportedOperationException();
    }

    AuthContext unauthorizedContext = new UnauthorizedContextImpl();

    static AuthContext unauthorized() {
        return unauthorizedContext;
    }

}
