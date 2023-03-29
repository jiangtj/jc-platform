package com.jtj.cloud.common.reactive;

import com.jtj.cloud.common.BaseException;
import com.jtj.cloud.common.BaseExceptionUtils;
import jakarta.annotation.Resource;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import static com.jtj.cloud.common.reactive.BaseExceptionHandler.ORDER;

/**
 * Created At 2021/3/26.
 */
@Order(ORDER)
public class BaseExceptionHandler implements WebExceptionHandler {

    public final static int ORDER = -100;

    @Resource
    private NoViewResponseContext context;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        if (throwable instanceof BaseException bex) {
            URIUtils.update(bex, exchange);
            return ServerResponse.from(bex)
                .flatMap(serverResponse -> serverResponse.writeTo(exchange, context));
        }
        if (throwable instanceof RuntimeException ex) {
            BaseException wrapper = BaseExceptionUtils.internalServerError(ex.getMessage(), ex);
            URIUtils.update(wrapper, exchange);
            return ServerResponse.from(wrapper)
                .flatMap(serverResponse -> serverResponse.writeTo(exchange, context));
        }
        return Mono.error(throwable);
    }
}
