package com.jtj.cloud.auth.context;

import com.jtj.cloud.auth.UserClaims;
import io.jsonwebtoken.Claims;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public record SystemUserContextImpl(UserClaims user, String token, Claims claims, Map<String, Object> ext) implements AuthContext {

    @Override
    public boolean isLogin() {
        return true;
    }

    @Override
    public SystemUserContextImpl put(String key, Object value) {
        if (ext.isEmpty()) {
            return fromSelf(this, Collections.singletonMap(key, value));
        }
        if (ext.size() == 1) {
            Map<String, Object> ext = new LinkedHashMap<>(2);
            ext.putAll(this.ext());
            ext.put(key, value);
            return fromSelf(this, ext);
        }
        this.ext.put(key, value);
        return this;
    }

    @Override
    public Object get(String key) {
        return this.ext.get(key);
    }

    private SystemUserContextImpl fromSelf(SystemUserContextImpl self, Map<String, Object> ext) {
        return new SystemUserContextImpl(self.user(), self.token(), self.claims(), ext);
    }
}
