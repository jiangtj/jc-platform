package com.jiangtj.cloud.auth.context;

import com.jiangtj.cloud.auth.TokenType;
import com.jiangtj.cloud.auth.rbac.RoleProvider;
import io.jsonwebtoken.Claims;

import java.util.List;

/**
 * Auth 上下文
 */
public interface AuthContext {

    String token();

    Claims claims();

    List<String> roles();

    RoleProvider roleProvider();

    default boolean isLogin() {
        return true;
    }

    default String type() {
        return claims().get(TokenType.KEY, String.class);
    }

    AuthContext unauthorizedInst = new UnauthorizedContextImpl();
    static AuthContext unauthorized() {
        return unauthorizedInst;
    }
}
