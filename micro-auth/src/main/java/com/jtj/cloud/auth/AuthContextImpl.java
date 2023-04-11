package com.jtj.cloud.auth;

import io.jsonwebtoken.Claims;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Auth 上下文
 * @param user 用户
 * @param token 源token
 * @param claims verifier后的body
 * @param ext 扩展
 */
public record AuthContextImpl(UserClaims user, String token, Claims claims, Map<String, Object> ext) implements AuthContext {

    @Override
    public boolean isLogin() {
        return true;
    }

    @Override
    public AuthContextImpl put(String key, Object value) {
        if (ext.isEmpty()) {
            return AuthContextImpl.from(this, Collections.singletonMap(key, value));
        }
        if (ext.size() == 1) {
            Map<String, Object> ext = new LinkedHashMap<>(2);
            ext.putAll(this.ext());
            ext.put(key, value);
            return AuthContextImpl.from(this, ext);
        }
        this.ext.put(key, value);
        return this;
    }

    @Override
    public Object get(String key) {
        return this.ext.get(key);
    }

    public static AuthContextImpl from(AuthContextImpl self, Map<String, Object> ext) {
        return new AuthContextImpl(self.user(), self.token(), self.claims(), ext);
    }

}
