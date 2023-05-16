package com.jiangtj.cloud.auth.rbac;

import com.jiangtj.cloud.auth.TokenType;
import com.jiangtj.cloud.auth.context.AuthContext;
import com.jiangtj.cloud.auth.context.AuthContextConverter;
import com.jiangtj.cloud.auth.context.SystemUserContextImpl;
import io.jsonwebtoken.Claims;

public class RoleAuthContextConverter implements AuthContextConverter {

    private final RoleProvider roleProvider;

    public RoleAuthContextConverter(RoleProvider roleProvider) {
        this.roleProvider = roleProvider;
    }

    @Override
    public String type() {
        return TokenType.SYSTEM_USER;
    }

    @Override
    public AuthContext convert(String token, Claims body) {
        return new SystemUserContextImpl(token, body, roleProvider);
    }
}
