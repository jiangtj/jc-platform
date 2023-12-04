package com.jiangtj.platform.auth.cloud.system;

import com.jiangtj.platform.auth.TokenType;
import com.jiangtj.platform.auth.cloud.JwtAuthContextConverter;
import com.jiangtj.platform.auth.context.JwtAuthContext;
import com.jiangtj.platform.auth.context.RoleProvider;
import io.jsonwebtoken.Claims;

public class SystemContextConverter implements JwtAuthContextConverter {

    private final RoleProvider roleProvider;

    public SystemContextConverter(RoleProvider roleProvider) {
        this.roleProvider = roleProvider;
    }

    @Override
    public String type() {
        return TokenType.SYSTEM_USER;
    }

    @Override
    public JwtAuthContext convert(String token, Claims body) {
        return new SystemUserContextImpl(token, body, roleProvider);
    }
}
