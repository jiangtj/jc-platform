package com.jiangtj.platform.auth.reactive;

import com.jiangtj.platform.auth.AuthRequestAttributes;
import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.auth.context.AuthContextFactory;
import jakarta.annotation.Resource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.jiangtj.platform.auth.reactive.ReactiveTokenFilter.ORDER;

@Order(ORDER)
public class ReactiveTokenFilter implements WebFilter {

    public static final int ORDER = -100;

    @Resource
    private AuthContextFactory authContextFactory;

    public static final String AUTH_CONTEXT_ATTRIBUTE = "J_PLATFORM_AUTh_ATTRIBUTE";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (request.getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        List<String> headers = request.getHeaders().get(AuthRequestAttributes.TOKEN_HEADER_NAME);
        if (headers == null || headers.size() != 1) {
            return chain.filter(exchange)
                .contextWrite(ctx -> ctx.put(AuthContext.class, AuthContext.unauthorized()));
        }

        String token = headers.get(0);
        AuthContext authCtx = authContextFactory.getAuthContext(token);

        exchange.getAttributes().put(AUTH_CONTEXT_ATTRIBUTE, authCtx);

        return chain.filter(exchange)
            .contextWrite(ctx -> ctx.put(AuthContext.class, authCtx));
    }
}
