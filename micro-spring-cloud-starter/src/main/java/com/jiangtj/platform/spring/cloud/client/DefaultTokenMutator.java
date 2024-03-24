package com.jiangtj.platform.spring.cloud.client;

import com.jiangtj.platform.spring.cloud.AuthServer;
import com.jiangtj.platform.spring.cloud.jwt.JwtAuthContext;
import jakarta.annotation.Resource;
import org.springframework.core.Ordered;

/**
 * 默认的 token 转换实现
 * 会保留除签名信息以外的其他信息
 * 签名信息包含 密钥id、签发者、授权目标、签发时间、过期时间
 */
public class DefaultTokenMutator implements TokenMutator, Ordered {

    @Resource
    private AuthServer authServer;

    @Override
    public boolean support(JwtAuthContext ctx) {
        return true;
    }

    @Override
    public String mutate(JwtAuthContext ctx, String target) {
        return authServer.createTokenFromClaim(ctx.claims(), target);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
