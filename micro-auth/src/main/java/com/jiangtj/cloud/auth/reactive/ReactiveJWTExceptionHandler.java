package com.jiangtj.cloud.auth.reactive;

import com.jiangtj.cloud.auth.AuthExceptionUtils;
import com.jiangtj.cloud.common.reactive.BaseExceptionHandler;
import io.jsonwebtoken.JwtException;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Order(BaseExceptionHandler.ORDER - 10)
public class ReactiveJWTExceptionHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof JwtException) {
            return Mono.error(AuthExceptionUtils.invalidToken(ex.getMessage(), ex));
        }
        return Mono.error(ex);
    }

}
