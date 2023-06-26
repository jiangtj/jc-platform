package com.jiangtj.cloud.auth.context;

import com.jiangtj.cloud.auth.AuthExceptionUtils;
import com.jiangtj.cloud.auth.rbac.RoleProvider;
import io.jsonwebtoken.Claims;

import java.util.List;

public class ServerContextImpl implements AuthContext {
    private final String token;
    private final Claims claims;

    public ServerContextImpl(String token, Claims claims) {
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
        throw AuthExceptionUtils.unLogin();
    }

    @Override
    public RoleProvider roleProvider() {
        throw AuthExceptionUtils.unLogin();
    }

}
