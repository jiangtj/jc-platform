package com.jiangtj.platform.spring.cloud;

import com.jiangtj.platform.auth.AuthRequestAttributes;
import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.spring.cloud.jwt.JwtAuthContext;
import com.jiangtj.platform.spring.cloud.jwt.TokenMutateService;
import jakarta.annotation.Resource;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

public class AuthWebClientFilter implements ExchangeFilterFunction {

    @Resource
    private TokenMutateService tokenMutateService;

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
            String token = tokenMutateService.mutate((JwtAuthContext) authCtx, request.url().getHost());
            ClientRequest filtered = ClientRequest.from(request)
                .header(AuthRequestAttributes.TOKEN_HEADER_NAME, token)
                .build();
            return next.exchange(filtered);
        });
    }
}
