package com.jiangtj.platform.gatewaysession;

import com.jiangtj.platform.common.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.casbin.casdoor.entity.User;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CasdoorAuthFilter implements GlobalFilter, Ordered {


    @Override public int getOrder() {
        return 0;
    }

    @Override public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return exchange.getSession().flatMap(webSession -> {
            User user = webSession.getAttribute("casdoorUser");
            log.error("-----ffff");
            log.error(JsonUtils.toJson(user));
            if (user != null) {
                return chain.filter(exchange);
            }
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add("Content-Type", "application/json");
            return response.setComplete();
        });
    }
}
