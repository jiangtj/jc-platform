package com.jtj.cloud.auth.reactive;

import com.jtj.cloud.auth.AuthContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface AuthReactorHolder {

    static Mono<ServerWebExchange> getExchange() {
        return Mono.deferContextual(ctx -> Mono.just(ctx.get(ServerWebExchange.class)));
    }

    static Mono<AuthContext> getAuthContext() {
        return Mono.deferContextual(ctx -> Mono.just(ctx.get(AuthContext.class)));
    }

}
