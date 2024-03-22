package com.jiangtj.platform.spring.cloud.jwt;

import com.jiangtj.platform.auth.context.AuthContext;
import io.jsonwebtoken.Claims;

/**
 * Auth 上下文
 */
public interface JwtAuthContext extends AuthContext {

    String token();

    Claims claims();

    default String provider() {
        return claims().get(PROVIDER, String.class);
    }

    @Override
    default String subject() {
        return claims().getSubject();
    }

    String PROVIDER = "provider";

}
