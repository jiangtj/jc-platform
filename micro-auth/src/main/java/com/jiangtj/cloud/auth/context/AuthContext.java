package com.jiangtj.cloud.auth.context;

import com.jiangtj.cloud.auth.TokenType;
import io.jsonwebtoken.Claims;

/**
 * Auth 上下文
 */
public interface AuthContext extends Context {

    String token();

    Claims claims();

    default String type() {
        return claims().get(TokenType.KEY, String.class);
    }

    default boolean isLogin() {
        return true;
    }

}
