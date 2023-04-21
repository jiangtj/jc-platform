package com.jtj.cloud.auth.sba;

import com.jtj.cloud.auth.AuthExceptionUtils;
import com.jtj.cloud.auth.AuthServer;
import com.jtj.cloud.auth.TokenType;
import com.jtj.cloud.auth.reactive.AuthReactiveWebFilter;
import com.jtj.cloud.auth.reactive.AuthReactorHandler;
import com.jtj.cloud.auth.reactive.AuthReactorUtils;
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
