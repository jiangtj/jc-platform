package com.jiangtj.cloud.auth.reactive;

import com.jiangtj.cloud.auth.RequestAttributes;
import com.jiangtj.cloud.auth.context.AuthContext;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

public class AuthWebClientFilter implements ExchangeFilterFunction {

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return Mono.deferContextual(ctx -> {
            if (!ctx.hasKey(AuthContext.class)) {
                return next.exchange(request);
            }
            AuthContext authCtx = ctx.get(AuthContext.class);
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
