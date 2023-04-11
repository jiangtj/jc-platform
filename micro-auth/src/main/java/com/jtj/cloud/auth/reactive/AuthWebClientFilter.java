package com.jtj.cloud.auth.reactive;

import com.jtj.cloud.auth.AuthServer;
import jakarta.annotation.Resource;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

public class AuthWebClientFilter implements ExchangeFilterFunction {

    @Resource
    AuthServer authServer;

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return Mono.deferContextual(ctx -> Mono.just(ctx.get(ServerWebExchange.class)))
            .flatMap(exchange -> {
                String headerName = authServer.getProperties().getHeaderName();
                List<String> authHeader = exchange.getRequest().getHeaders().get(headerName);
                if (authHeader == null || authHeader.size() !=1) {
                    return next.exchange(request);
                }
                ClientRequest filtered = ClientRequest.from(request)
                    .header(headerName, authHeader.get(0))
                    .build();
                return next.exchange(filtered);
            });
    }
}
