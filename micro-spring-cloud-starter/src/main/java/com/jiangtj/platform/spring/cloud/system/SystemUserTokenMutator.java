package com.jiangtj.platform.spring.cloud.system;

import com.jiangtj.platform.spring.cloud.AuthServer;
import com.jiangtj.platform.spring.cloud.Providers;
import com.jiangtj.platform.spring.cloud.client.TokenMutator;
import com.jiangtj.platform.spring.cloud.jwt.JwtAuthContext;
import jakarta.annotation.Resource;

public class SystemUserTokenMutator implements TokenMutator {

    @Resource
    private AuthServer authServer;

    @Override
    public boolean support(JwtAuthContext ctx) {
        return Providers.SYSTEM_USER.equals(ctx.provider());
    }

    @Override
    public String mutate(JwtAuthContext ctx, String target) {
        return authServer.createUserTokenFromClaim(ctx.claims(), target);
    }
}
