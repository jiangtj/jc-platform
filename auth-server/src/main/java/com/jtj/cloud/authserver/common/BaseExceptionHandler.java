package com.jtj.cloud.authserver.common;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

/**
 * Created At 2021/3/26.
 */
@Slf4j
@Order(-100)
public class BaseExceptionHandler implements WebExceptionHandler {

    @Resource
    private NoViewResponseContext context;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof BaseException baseException) {
            log.error(ex.getClass().getName(), baseException);
            return ServerResponse.from(baseException)
                .flatMap(serverResponse -> serverResponse.writeTo(exchange, context));
        }
        return Mono.error(ex);
    }
}
