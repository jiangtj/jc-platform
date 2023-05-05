package com.jiangtj.cloud.auth.reactive;

import com.jiangtj.cloud.auth.AuthExceptionUtils;
import com.jiangtj.cloud.auth.AuthUtils;
import com.jiangtj.cloud.auth.TokenType;
import com.jiangtj.cloud.auth.context.AuthContext;
import com.jiangtj.cloud.common.BaseExceptionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

public interface AuthReactorUtils {

    static Function<AuthContext, Mono<AuthContext>> tokenTypeHandler(String type) {
        return ctx -> {
            if (!type.equals(ctx.claims().get(TokenType.KEY))) {
                return Mono.error(BaseExceptionUtils.forbidden("不允许访问 todo"));
            }
            return Mono.just(ctx);
        };
    }

    static Mono<AuthContext> isTokenType(String type) {
        return AuthReactorHolder.deferAuthContext()
            .flatMap(tokenTypeHandler(type));
    }

    static <T> Function<T, Mono<T>> tokenTypeInterceptor(String type) {
        return t -> isTokenType(type).thenReturn(t);
    }

    static Function<AuthContext, Mono<AuthContext>> hasLoginHandler() {
        return ctx -> {
            if (!ctx.isLogin()) {
                return Mono.error(AuthExceptionUtils.unLogin());
            }
            return Mono.just(ctx);
        };
    }

    static Mono<AuthContext> hasLogin() {
        return AuthReactorHolder.deferAuthContext()
            .flatMap(AuthReactorUtils.hasLoginHandler());
    }

    static <T> Mono<T> hasLogin(T val) {
        return hasLogin().then(Mono.just(val));
    }

    static <T> Function<T, Mono<T>> loginInterceptor() {
        return t -> hasLogin().thenReturn(t);
    }

    static Function<AuthContext, Mono<AuthContext>> hasRoleHandler(String... roles) {
        return ctx -> {
            List<String> userRoles = ctx.user().roles();
            return Flux.just(roles)
                .map(AuthUtils::toKey)
                .doOnNext(role -> {
                    if (!userRoles.contains(role)) {
                        throw AuthExceptionUtils.noRole(role);
                    }
                })
                .then(Mono.just(ctx));
        };
    }

    static Mono<AuthContext> hasRole(String... roles) {
        return AuthReactorHolder.deferAuthContext()
            .flatMap(hasRoleHandler(roles));
    }

    static <T> Function<T, Mono<T>> roleInterceptor(String... roles) {
        return t -> hasLogin(roles).thenReturn(t);
    }

    static Function<AuthContext, Mono<AuthContext>> hasPermissionHandler(String... permissions) {
        return ctx -> {
            List<String> userPermissions = ctx.permissions();
            return Flux.just(permissions)
                .doOnNext(perm -> {
                    if (!userPermissions.contains(perm)) {
                        throw AuthExceptionUtils.noPermission(perm);
                    }
                })
                .then(Mono.just(ctx));
        };
    }

    static Mono<AuthContext> hasPermission(String... permissions) {
        return AuthReactorHolder.deferAuthContext()
            .flatMap(hasPermissionHandler(permissions));
    }

    static <T> Function<T, Mono<T>> permissionInterceptor(String... permissions) {
        return t -> hasPermission(permissions).thenReturn(t);
    }

}
