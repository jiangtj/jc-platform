package com.jiangtj.cloud.auth.sba;

import com.jiangtj.cloud.auth.AuthExceptionUtils;
import com.jiangtj.cloud.auth.AuthServer;
import com.jiangtj.cloud.auth.TokenType;
import com.jiangtj.cloud.auth.reactive.AuthReactiveWebFilter;
import com.jiangtj.cloud.auth.reactive.AuthReactorHandler;
import com.jiangtj.cloud.auth.reactive.AuthReactorUtils;
import jakarta.annotation.Resource;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

public class AuthActuatorReactorWebFilter extends AuthReactiveWebFilter {

    @Resource
    private AuthServer authServer;

    @Override
    public List<String> getIncludePatterns() {
        return Collections.singletonList("/actuator/**");
    }

    @Override
    public List<String> getExcludePatterns() {
        return Collections.emptyList();
    }

    @Override
    public AuthReactorHandler filter(AuthReactorHandler handler) {
        return handler.hasLogin()
            // .isTokenType(TokenType.SERVER)
            .filter(ctx -> {
                Object tokenType = ctx.claims().get(TokenType.KEY);
                if (TokenType.SERVER.equals(tokenType)) {
                    if (!authServer.getApplicationName().equals(ctx.claims().getAudience())) {
                        return Mono.error(AuthExceptionUtils.invalidToken("不支持访问当前服务", null));
                    }
                    return Mono.just(ctx);
                }
                if (TokenType.SYSTEM_USER.equals(tokenType)) {
                    return Mono.just(ctx)
                        .flatMap(AuthReactorUtils.hasRoleHandler(RoleInst.ACTUATOR.name()));
                }
                return Mono.error(AuthExceptionUtils.invalidToken("不支持访问当前服务", null));
            });
    }
}
