package com.jiangtj.platform.auth.reactive;

import com.jiangtj.platform.auth.AuthExceptionUtils;
import com.jiangtj.platform.web.Orders;
import io.jsonwebtoken.JwtException;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Order(Orders.BASE_EXCEPTION_FILTER - 10)
public class ReactiveJWTExceptionHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof JwtException) {
            return Mono.error(AuthExceptionUtils.invalidToken(ex.getMessage(), ex));
        }
        return Mono.error(ex);
    }

}
