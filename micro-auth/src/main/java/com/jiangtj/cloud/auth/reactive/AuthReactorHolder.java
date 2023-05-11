package com.jiangtj.cloud.auth.reactive;

import com.jiangtj.cloud.auth.context.Context;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface AuthReactorHolder  {

    static Mono<ServerWebExchange> deferExchange() {
        return Mono.deferContextual(ctx -> Mono.just(ctx.get(ServerWebExchange.class)));
    }

    static Mono<Context> deferAuthContext() {
        return Mono.deferContextual(ctx -> Mono.just(ctx.get(Context.class)));
    }

}
