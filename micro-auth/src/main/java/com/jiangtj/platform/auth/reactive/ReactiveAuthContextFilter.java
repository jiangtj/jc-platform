package com.jiangtj.platform.auth.reactive;

import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.auth.context.AuthContextFactory;
import com.jiangtj.platform.common.reactive.BaseExceptionHandler;
import jakarta.annotation.Resource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@Order(BaseExceptionHandler.ORDER + 10)
public class ReactiveAuthContextFilter implements WebFilter {

    @Resource
    private AuthContextFactory factory;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (request.getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        AuthContext context = factory.getAuthContext(request);

        if (context == null) {
            return chain.filter(exchange);
        }

        return chain.filter(exchange)
            .contextWrite(ctx -> ctx.put(AuthContext.class, context));
    }
}
