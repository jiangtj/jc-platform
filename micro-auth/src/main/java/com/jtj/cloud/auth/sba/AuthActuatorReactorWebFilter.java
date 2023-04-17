package com.jtj.cloud.auth.sba;

import com.jtj.cloud.auth.AuthExceptionUtils;
import com.jtj.cloud.auth.AuthServer;
import com.jtj.cloud.auth.TokenType;
import com.jtj.cloud.auth.reactive.AuthReactiveWebFilter;
import com.jtj.cloud.auth.reactive.AuthReactorHandler;
import com.jtj.cloud.auth.reactive.AuthReactorHolder;
import com.jtj.cloud.common.BaseExceptionUtils;
import io.jsonwebtoken.Claims;
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
    public Mono<Void> filter(AuthReactorHandler handler) {
        return AuthReactorHolder.deferAuthContext()
            .flatMap(ctx -> {
                Claims claims = ctx.claims();
                if (!TokenType.SERVER.equals(claims.get(TokenType.KEY))) {
                    return Mono.error(BaseExceptionUtils.forbidden("当前token不允许访问actuator端口"));
                }
                if (!authServer.getApplicationName().equals(claims.getAudience())) {
                    return Mono.error(AuthExceptionUtils.invalidToken("不支持访问当前服务", null));
                }
                return Mono.empty();
            })
            .then();
    }
}
