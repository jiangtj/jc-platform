package com.jtj.cloud.authserver;

import com.jtj.cloud.authserver.common.BaseExceptionUtils;
import com.jtj.cloud.authserver.common.NoViewResponseContext;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Slf4j
@Order(-101)
public class ReactiveJWTExceptionHandler implements WebExceptionHandler {

    @Resource
    private NoViewResponseContext context;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof JwtException) {
            log.error(ex.getClass().getName(), ex);
            return Mono.error(BaseExceptionUtils.invalidToken(ex.getMessage(), ex));
        }
        return Mono.error(ex);
    }
}
