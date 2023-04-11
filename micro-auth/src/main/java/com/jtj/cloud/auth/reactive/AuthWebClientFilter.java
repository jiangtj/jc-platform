package com.jtj.cloud.auth.reactive;

import com.jtj.cloud.auth.RequestAttributes;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

public class AuthWebClientFilter implements ExchangeFilterFunction {

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return AuthReactorHolder.getAuthContext().flatMap(authCtx -> {
            if (!authCtx.isLogin()) {
                return next.exchange(request);
            }
            String token = authCtx.token();
            ClientRequest filtered = ClientRequest.from(request)
                .header(RequestAttributes.TOKEN_HEADER_NAME, token)
                .build();
            return next.exchange(filtered);
        });
    }
}
