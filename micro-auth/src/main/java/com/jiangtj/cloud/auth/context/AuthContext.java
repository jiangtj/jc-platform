package com.jiangtj.cloud.auth.context;

import com.jiangtj.cloud.auth.TokenType;
import io.jsonwebtoken.Claims;

import java.util.List;

/**
 * Auth 上下文
 */
public interface AuthContext {

    boolean isLogin();

    String token();

    Claims claims();

    List<String> roles();

    List<String> permissions();

    default String type() {
        return claims().get(TokenType.KEY, String.class);
    }

    static AuthContext unauthorized() {
        return UnauthorizedContextImpl.self;
    }
}
