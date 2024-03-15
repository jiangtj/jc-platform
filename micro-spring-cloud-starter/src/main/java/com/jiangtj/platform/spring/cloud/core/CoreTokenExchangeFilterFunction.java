package com.jiangtj.platform.spring.cloud.core;

import com.jiangtj.platform.spring.cloud.AuthServer;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.jiangtj.platform.auth.AuthRequestAttributes.TOKEN_HEADER_NAME;

public class CoreTokenExchangeFilterFunction implements ExchangeFilterFunction {

    @Resource
    private ObjectProvider<AuthServer> authServers;

    public String createToken() {
        return Objects.requireNonNull(authServers.getIfUnique())
            .createServerToken("core-server");
    }

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        ClientRequest filtered = ClientRequest.from(request)
            .header(TOKEN_HEADER_NAME, createToken())
            .build();
        return next.exchange(filtered);
    }
}
