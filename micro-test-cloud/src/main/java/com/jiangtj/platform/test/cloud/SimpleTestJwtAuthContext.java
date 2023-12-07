package com.jiangtj.platform.test.cloud;

import com.jiangtj.platform.auth.context.JwtAuthContext;
import io.jsonwebtoken.Claims;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SimpleTestJwtAuthContext implements JwtAuthContext {

    private final String subject;
    private final List<String> roles;
    private final List<String> permissions;
    private final Claims claims;

    public SimpleTestJwtAuthContext(String subject, List<String> roles, List<String> permissions) {
        this.subject = subject;
        this.roles = roles;
        this.permissions = permissions;
        this.claims = new TestClaims(this);
    }

    @Override
    public boolean isLogin() {
        return true;
    }

    @Override
    public String token() {
        return "";
    }

    @Override
    public Claims claims() {
        return claims;
    }

    @Override
    public String type() {
        return "test";
    }

    @Override
    public String subject() {
        return subject;
    }

    @Override
    public List<String> roles() {
        return roles;
    }

    @Override
    public List<String> permissions() {
        return permissions;
    }

    static class TestClaims extends HashMap<String, Object> implements Claims {

        private SimpleTestJwtAuthContext ctx;

        public TestClaims(SimpleTestJwtAuthContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public String getIssuer() {
            return "test-runner";
        }

        @Override
        public String getSubject() {
            return ctx.subject();
        }

        @Override
        public Set<String> getAudience() {
            return Set.of("testcase");
        }

        @Override
        public Date getExpiration() {
            return Date.from(Instant.now().plusSeconds(10));
        }

        @Override
        public Date getNotBefore() {
            return null;
        }

        @Override
        public Date getIssuedAt() {
            return Date.from(Instant.now());
        }

        @Override
        public String getId() {
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T get(String claimName, Class<T> requiredType) {
            return (T) super.get(claimName);
        }


    }
}
