package com.jiangtj.cloud.auth.context;

import com.jiangtj.cloud.auth.AuthExceptionUtils;
import com.jiangtj.cloud.auth.rbac.RoleProvider;
import io.jsonwebtoken.Claims;

import java.util.List;

public abstract class AbstractSimpleAuthContext implements AuthContext {
    private String token;
    private Claims claims;

    public AbstractSimpleAuthContext(String token, Claims claims) {
        this.token = token;
        this.claims = claims;
    }

    @Override
    public String token() {
        return token;
    }

    @Override
    public Claims claims() {
        return claims;
    }

    @Override
    public List<String> roles() {
        throw AuthExceptionUtils.unSupport();
    }

    @Override
    public List<String> permissions() {
        throw AuthExceptionUtils.unSupport();
    }

    @Override
    public boolean isLogin() {
        return true;
    }
}
