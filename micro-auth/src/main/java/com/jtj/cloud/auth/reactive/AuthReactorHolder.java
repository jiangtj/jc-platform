package com.jtj.cloud.auth.reactive;

import com.jtj.cloud.auth.context.AuthContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface AuthReactorHolder  {

    static Mono<ServerWebExchange> deferExchange() {
        return Mono.deferContextual(ctx -> Mono.just(ctx.get(ServerWebExchange.class)));
    }

    static Mono<AuthContext> deferAuthContext() {
        return Mono.deferContextual(ctx -> Mono.just(ctx.get(AuthContext.class)));
    }

}
