package com.jtj.cloud.auth.reactive;

import com.jtj.cloud.auth.AuthExceptionUtils;
import reactor.core.publisher.Mono;

public interface AuthReactorUtils {

    static Mono<Void> hasLogin() {
        return AuthReactorHolder.getAuthContext()
            .flatMap(ctx -> {
                if (!ctx.isLogin()) {
                    return Mono.error(AuthExceptionUtils.unLogin());
                }
                return Mono.empty();
            });
    }

    static <T> Mono<T> hasLogin(T val) {
        return AuthReactorUtils
            .hasLogin()
            .then(Mono.just(val));
    }

}
