package com.jiangtj.cloud.auth.reactive;

import com.jiangtj.cloud.auth.TokenMutateService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import reactivefeign.client.ReactiveHttpRequest;
import reactivefeign.client.ReactiveHttpRequestInterceptor;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static com.jiangtj.cloud.auth.AuthRequestAttributes.TOKEN_HEADER_NAME;

@Slf4j
public class ReactiveAuthFeignInterceptor implements ReactiveHttpRequestInterceptor {

    @Resource
    private TokenMutateService tokenMutateService;

    @Override
    public Mono<ReactiveHttpRequest> apply(ReactiveHttpRequest request) {
        return AuthReactorHolder.deferAuthContext()
                .flatMap(ctx -> {
                    if (ctx == null || !ctx.isLogin()) {
                        return Mono.just(request);
                    }
                    String name = request.target().name();
                    request.headers().put(TOKEN_HEADER_NAME, Collections.singletonList(tokenMutateService.mutate(ctx, name)));
                    return Mono.just(request);
                });
    }

}
