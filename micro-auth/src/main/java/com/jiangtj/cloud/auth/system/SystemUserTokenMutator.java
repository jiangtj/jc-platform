package com.jiangtj.cloud.auth.system;

import com.jiangtj.cloud.auth.AuthServer;
import com.jiangtj.cloud.auth.TokenMutator;
import com.jiangtj.cloud.auth.TokenType;
import com.jiangtj.cloud.auth.context.AuthContext;
import jakarta.annotation.Resource;

public class SystemUserTokenMutator implements TokenMutator {

    @Resource
    private AuthServer authServer;

    @Override
    public boolean support(AuthContext ctx) {
        return TokenType.SYSTEM_USER.equals(ctx.type());
    }

    @Override
    public String mutate(AuthContext ctx, String target) {
        return authServer.createUserTokenFromClaim(ctx.claims(), target);
    }
}
