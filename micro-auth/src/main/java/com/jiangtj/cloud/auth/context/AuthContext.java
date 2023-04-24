package com.jiangtj.cloud.auth.context;

import com.jiangtj.cloud.auth.AuthHolder;
import com.jiangtj.cloud.auth.UserClaims;
import io.jsonwebtoken.Claims;

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
        return AuthHolder.getRoleProvider()
            .getPermissionKeys(roles().toArray(String[]::new));
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
