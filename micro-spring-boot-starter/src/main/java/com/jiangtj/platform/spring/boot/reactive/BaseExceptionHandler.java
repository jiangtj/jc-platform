package com.jiangtj.platform.spring.boot.reactive;

import com.jiangtj.platform.common.JsonUtils;
import com.jiangtj.platform.web.BaseException;
import com.jiangtj.platform.web.BaseExceptionUtils;
import com.jiangtj.platform.web.Orders;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Slf4j
@Order(Orders.BASE_EXCEPTION_FILTER)
public class BaseExceptionHandler implements WebExceptionHandler {

    @Resource
    private NoViewResponseContext context;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        if (throwable instanceof ErrorResponseException bex) {
            URIUtils.update(bex, exchange);
            log.error(JsonUtils.toJson(bex.getBody()));
            return ServerResponse.from(bex)
                .flatMap(serverResponse -> serverResponse.writeTo(exchange, context));
        }
        if (throwable instanceof RuntimeException ex) {
            BaseException wrapper = BaseExceptionUtils.internalServerError(ex.getMessage(), ex);
            URIUtils.update(wrapper, exchange);
            log.error("RuntimeException Handler", ex);
            return ServerResponse.from(wrapper)
                .flatMap(serverResponse -> serverResponse.writeTo(exchange, context));
        }
        return Mono.error(throwable);
    }
}
