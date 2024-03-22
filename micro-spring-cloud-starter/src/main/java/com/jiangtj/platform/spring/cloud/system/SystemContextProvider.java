package com.jiangtj.platform.spring.cloud.system;

import com.jiangtj.platform.auth.context.RoleProvider;
import com.jiangtj.platform.spring.cloud.Providers;
import com.jiangtj.platform.spring.cloud.jwt.JwtAuthContext;
import com.jiangtj.platform.spring.cloud.jwt.JwtAuthContextProvider;
import io.jsonwebtoken.Claims;

public class SystemContextProvider implements JwtAuthContextProvider {

    private final RoleProvider roleProvider;

    public SystemContextProvider(RoleProvider roleProvider) {
        this.roleProvider = roleProvider;
    }

    @Override
    public String provider() {
        return Providers.SYSTEM_USER;
    }

    @Override
    public JwtAuthContext convert(String token, Claims body) {
        return new SystemUserContextImpl(token, body, roleProvider);
    }
}
