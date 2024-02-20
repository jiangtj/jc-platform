package com.jiangtj.platform.sql.r2dbc;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface R2dbcExchangeHolder {

    static Mono<ServerWebExchange> deferExchange() {
        return Mono.deferContextual(ctx -> Mono.just(ctx.get(ServerWebExchange.class)));
    }

}
