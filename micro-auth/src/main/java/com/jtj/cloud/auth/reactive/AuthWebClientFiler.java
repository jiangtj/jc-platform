package com.jtj.cloud.auth.reactive;

import com.jtj.cloud.auth.AuthServer;
import jakarta.annotation.Resource;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

public class AuthWebClientFiler {

    @Resource
    AuthServer authServer;

    public ExchangeFilterFunction filter(ServerWebExchange exchange) {
        return (request,  next) -> {
            String headerName = authServer.getProperties().getHeaderName();
            List<String> authHeader = exchange.getRequest().getHeaders().get(headerName);
            if (authHeader == null || authHeader.size() !=1) {
                return next.exchange(request);
            }
            ClientRequest filtered = ClientRequest.from(request)
                .header(headerName, authHeader.get(0))
                .build();
            return next.exchange(filtered);
        };
    }



}
