package com.jiangtj.platform.spring.boot.reactive;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface JCloudReactorHolder {

    static Mono<ServerWebExchange> deferExchange() {
        return Mono.deferContextual(ctx -> Mono.just(ctx.get(ServerWebExchange.class)));
    }

}
