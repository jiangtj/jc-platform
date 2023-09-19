package com.jiangtj.cloud.common.reactive;

import com.jiangtj.cloud.common.BaseException;
import com.jiangtj.cloud.common.BaseExceptionUtils;
import com.jiangtj.cloud.common.utils.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import static com.jiangtj.cloud.common.reactive.BaseExceptionHandler.ORDER;

@Slf4j
@Order(ORDER)
public class BaseExceptionHandler implements WebExceptionHandler {

    public final static int ORDER = -100;

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
