package com.jiangtj.platform.spring.cloud.server;

import com.jiangtj.platform.auth.AuthExceptionUtils;
import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.auth.reactive.AuthReactorHolder;
import com.jiangtj.platform.auth.reactive.aop.ReactiveAnnotationMethodBeforeAdvice;
import com.jiangtj.platform.web.ApplicationProperty;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
public class ServerTokenReactiveAdvice extends ReactiveAnnotationMethodBeforeAdvice<ServerToken> implements Ordered {

    @Resource
    private ApplicationProperty applicationProperty;

    @Override
    public Class<ServerToken> getAnnotationType() {
        return ServerToken.class;
    }

    @Override
    public Mono<Void> before(List<ServerToken> annotations, Object[] args) {
        Mono<AuthContext> context = AuthReactorHolder.deferAuthContext();
        for (ServerToken annotation : annotations) {
            context = context.flatMap(ctx -> checkServerToken(ctx, annotation.value()));
        }
        return context.then();
    }

    private Mono<AuthContext> checkServerToken(AuthContext ctx, String[] sources) {
        if (ServerTokenUtils.check(ctx, applicationProperty.getName(), sources)) {
            return Mono.just(ctx);
        }
        return Mono.error(AuthExceptionUtils.invalidToken("不支持的 Auth Context", null));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}
