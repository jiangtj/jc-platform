package com.jtj.cloud.auth;

import com.jtj.cloud.auth.rbac.Permission;
import com.jtj.cloud.auth.rbac.Role;
import com.jtj.cloud.common.BaseExceptionUtils;
import io.jsonwebtoken.Claims;

import java.util.*;

/**
 * Auth 上下文
 */
public interface AuthContext {

    boolean isLogin();

    UserClaims user();

    String token();

    Claims claims();

    default List<String> permissions() {
        return user().roles().stream()
            .flatMap(roleKey -> AuthHolder.getRoleContext().getRoles().stream()
                .filter(role -> role.name().equals(roleKey))
                .map(Role::permissions)
                .flatMap(Collection::stream))
            .map(Permission::toString)
            .toList();
    }

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
        List<String> roleList = Optional.ofNullable(body.get("role", String.class))
            .map(r -> r.split(","))
            .map(Arrays::asList)
            .orElse(Collections.emptyList());
        return new AuthContextImpl(new UserClaims(subject, roleList), token, body, Collections.emptyMap());
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

    record AuthContextImpl(UserClaims user, String token, Claims claims, Map<String, Object> ext) implements AuthContext {

        @Override
        public boolean isLogin() {
            return true;
        }

        @Override
        public AuthContextImpl put(String key, Object value) {
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

        private AuthContextImpl fromSelf(AuthContextImpl self, Map<String, Object> ext) {
            return new AuthContextImpl(self.user(), self.token(), self.claims(), ext);
        }
    }

}
