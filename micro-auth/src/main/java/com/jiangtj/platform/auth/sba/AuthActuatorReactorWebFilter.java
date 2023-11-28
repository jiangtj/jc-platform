package com.jiangtj.platform.auth.sba;

import com.jiangtj.platform.auth.AuthExceptionUtils;
import com.jiangtj.platform.auth.AuthServer;
import com.jiangtj.platform.auth.TokenType;
import com.jiangtj.platform.auth.reactive.AuthReactiveWebFilter;
import com.jiangtj.platform.auth.reactive.AuthReactorHandler;
import com.jiangtj.platform.auth.reactive.AuthReactorUtils;
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
                String tokenType = ctx.type();
                Set<String> audience = ctx.claims().getAudience();
                if (!audience.contains(authServer.getApplicationName())) {
                    return Mono.error(AuthExceptionUtils.invalidToken("不支持访问当前服务", null));
                }
                if (!TokenType.SERVER.equals(tokenType)) {
                    return Mono.just(ctx).flatMap(AuthReactorUtils.hasRoleHandler(RoleInst.ACTUATOR.name()));
                }
                return Mono.just(ctx);
            });
    }
}
