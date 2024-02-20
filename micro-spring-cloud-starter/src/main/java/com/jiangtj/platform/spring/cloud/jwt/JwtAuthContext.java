package com.jiangtj.platform.spring.cloud.jwt;

import com.jiangtj.platform.auth.TokenType;
import com.jiangtj.platform.auth.context.AuthContext;
import io.jsonwebtoken.Claims;

/**
 * Auth 上下文
 */
public interface JwtAuthContext extends AuthContext {

    String token();

    Claims claims();

    default String type() {
        return claims().get(TokenType.KEY, String.class);
    }

    default String subject() {
        return claims().getSubject();
    }

}
