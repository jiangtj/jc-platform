package com.jtj.cloud.common.reactive;

import com.jtj.cloud.common.BaseException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import static com.jtj.cloud.common.reactive.BaseExceptionHandler.ORDER;

/**
 * Created At 2021/3/26.
 */
@Slf4j
@Order(ORDER)
public class BaseExceptionHandler implements WebExceptionHandler {

    public final static int ORDER = -100;

    @Resource
    private NoViewResponseContext context;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof BaseException baseException) {
            URIUtils.update(baseException, exchange);
            return ServerResponse.from(baseException)
                .flatMap(serverResponse -> serverResponse.writeTo(exchange, context));
        }
        return Mono.error(ex);
    }
}
