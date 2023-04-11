package com.jtj.cloud.auth;

import com.jtj.cloud.common.BaseExceptionUtils;
import io.jsonwebtoken.Claims;

import java.util.Collections;

/**
 * Auth 上下文
 */
public interface AuthContext {

    boolean isLogin();

    UserClaims user();

    String token();

    Claims claims();

    default AuthContext put(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    default Object get(String key) {
        throw new UnsupportedOperationException();
    }

    AuthContext unauthorizedContext = new UnauthorizedContext();

    static AuthContext unauthorized() {
        return unauthorizedContext;
    }

    static AuthContext from(AuthServer authServer, String token) {
        Claims body = authServer.verifier().verify(token).getBody();
        String subject = body.getSubject();
        return new AuthContextImpl(
            new UserClaims(subject, Collections.emptyList()),
            token,
            body,
            Collections.emptyMap());
    }

    class UnauthorizedContext implements AuthContext {

        @Override
        public boolean isLogin() {
            return false;
        }

        @Override
        public UserClaims user() {
            throw BaseExceptionUtils.unauthorized("未授权");
        }

        @Override
        public String token() {
            throw BaseExceptionUtils.unauthorized("未授权");
        }

        @Override
        public Claims claims() {
            throw BaseExceptionUtils.unauthorized("未授权");
        }
    }

}
