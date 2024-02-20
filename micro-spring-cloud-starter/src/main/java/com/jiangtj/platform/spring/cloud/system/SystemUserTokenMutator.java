package com.jiangtj.platform.spring.cloud.system;

import com.jiangtj.platform.auth.TokenMutator;
import com.jiangtj.platform.auth.TokenType;
import com.jiangtj.platform.auth.context.JwtAuthContext;
import com.jiangtj.platform.spring.cloud.AuthServer;
import jakarta.annotation.Resource;

public class SystemUserTokenMutator implements TokenMutator {

    @Resource
    private AuthServer authServer;

    @Override
    public boolean support(JwtAuthContext ctx) {
        return TokenType.SYSTEM_USER.equals(ctx.type());
    }

    @Override
    public String mutate(JwtAuthContext ctx, String target) {
        return authServer.createUserTokenFromClaim(ctx.claims(), target);
    }
}
