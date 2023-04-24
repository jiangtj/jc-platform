package com.jiangtj.cloud.common.reactive;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 在 spring security 中，包含 ServerWebExchange，其他地方不包含，这里修复 ServerWebExchange Context
 */
public class FixServerWebExchangeContext implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange)
            .contextWrite(ctx -> {
                if (ctx.hasKey(ServerWebExchange.class)) {
                    return ctx;
                }
                return ctx.put(ServerWebExchange.class, exchange);
            });
    }
}
