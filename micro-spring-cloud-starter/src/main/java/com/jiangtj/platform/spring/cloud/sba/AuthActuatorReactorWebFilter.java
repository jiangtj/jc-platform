package com.jiangtj.platform.spring.cloud.sba;

import com.jiangtj.platform.auth.AuthExceptionUtils;
import com.jiangtj.platform.auth.TokenType;
import com.jiangtj.platform.auth.reactive.AuthReactiveWebFilter;
import com.jiangtj.platform.auth.reactive.AuthReactorHandler;
import com.jiangtj.platform.auth.reactive.AuthReactorUtils;
import com.jiangtj.platform.spring.cloud.AuthServer;
import com.jiangtj.platform.spring.cloud.jwt.JwtAuthContext;
import jakarta.annotation.Resource;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class AuthActuatorReactorWebFilter extends AuthReactiveWebFilter {

    @Resource
    private AuthServer authServer;

    @Override
    public List<String> getIncludePatterns() {
        return Collections.singletonList("/actuator/**");
    }

    @Override
    public List<String> getExcludePatterns() {
        return super.getExcludePatterns();
    }

    @Override
    public void filter(AuthReactorHandler handler) {
        handler.hasLogin()
            // .isTokenType(TokenType.SERVER)
            .filter(ctx -> {
                if (ctx instanceof JwtAuthContext jwtCtx) {
                    String tokenType = jwtCtx.type();
                    Set<String> audience = jwtCtx.claims().getAudience();
                    if (!audience.contains(authServer.getApplicationName())) {
                        return Mono.error(AuthExceptionUtils.invalidToken("不支持访问当前服务", null));
                    }
                    if (!TokenType.SERVER.equals(tokenType)) {
                        return Mono.just(ctx).flatMap(AuthReactorUtils.hasRoleHandler(RoleInst.ACTUATOR.name()));
                    }
                    return Mono.just(ctx);
                } else {
                    return Mono.error(AuthExceptionUtils.invalidToken("不支持的 Auth Context", null));
                }
            });
    }
}
