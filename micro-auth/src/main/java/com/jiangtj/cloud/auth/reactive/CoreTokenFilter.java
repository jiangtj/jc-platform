package com.jiangtj.cloud.auth.reactive;

import com.jiangtj.cloud.auth.AuthRequestAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static com.jiangtj.cloud.auth.reactive.ReactiveTokenFilter.ORDER;

@Order(ORDER - 10)
public class CoreTokenFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        String path = exchange.getRequest().getPath().value();
        if (path.startsWith("/service/core-server")) {
            exchange.mutate().request(request.mutate().headers(httpHeaders -> {
                httpHeaders.remove(AuthRequestAttributes.TOKEN_HEADER_NAME);
            }).build());
        }
        return chain.filter(exchange);
    }
}
