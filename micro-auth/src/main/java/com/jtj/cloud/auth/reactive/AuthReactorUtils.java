package com.jtj.cloud.auth.reactive;

import com.jtj.cloud.auth.AuthExceptionUtils;
import com.jtj.cloud.common.BaseExceptionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

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

    static <T> Function<T, Mono<T>> loginInterceptor() {
        return t -> hasLogin().thenReturn(t);
    }

    static Mono<Void> hasRole(String... roles) {
        return AuthReactorHolder.getAuthContext()
            .map(ctx -> ctx.user().roles())
            .flatMap(userRoles ->Flux.just(roles)
                .doOnNext(role -> {
                    if (!userRoles.contains(role)) {
                        throw BaseExceptionUtils.unauthorized("1");
                    }
                })
                .then())
            .then();
    }

    static <T> Function<T, Mono<T>> roleInterceptor(String... roles) {
        return t -> hasLogin(roles).thenReturn(t);
    }

}
