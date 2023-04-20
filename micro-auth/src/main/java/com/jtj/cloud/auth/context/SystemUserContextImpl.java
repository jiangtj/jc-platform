package com.jtj.cloud.auth.context;

import com.jtj.cloud.auth.UserClaims;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
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

    @Override
    public List<String> roles() {
        List<String> roles = AuthContext.super.roles();
        if (getId() == 1 && !roles.contains("system")) {
            log.warn("ID:1 is super role, must have system role, but don't have now, please add it.");
            List<String> roleArr = new ArrayList<>(roles);
            roleArr.add("system");
            return roleArr;
        }
        return roles;
    }

    @Override
    public List<String> permissions() {
        return AuthContext.super.permissions();
    }

    public long getId() {
        return Long.parseLong(user.id());
    }

    private SystemUserContextImpl fromSelf(SystemUserContextImpl self, Map<String, Object> ext) {
        return new SystemUserContextImpl(self.user(), self.token(), self.claims(), ext);
    }
}
