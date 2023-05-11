package com.jiangtj.cloud.auth.reactive;

import com.jiangtj.cloud.auth.context.Context;
import com.jiangtj.cloud.auth.context.RoleAuthContext;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class AuthReactorHandler {

    private String tokenType;
    private boolean loginCheck = false;
    private String[] roles;
    private Function<Context, Mono<Context>> fn;

    public AuthReactorHandler() {
    }

    public AuthReactorHandler isTokenType(String type) {
        this.tokenType = type;
        return this;
    }

    public AuthReactorHandler hasLogin() {
        this.loginCheck = true;
        return this;
    }

    public AuthReactorHandler hasRole(String... roles) {
        this.roles = roles;
        return this;
    }

    public AuthReactorHandler filter(Function<Context, Mono<Context>> fn) {
        this.fn = fn;
        return this;
    }

    public <V> Mono<V> next(Mono<V> next) {
        Mono<Context> chain = AuthReactorHolder.deferAuthContext();
        if (tokenType != null) {
            chain = chain.flatMap(AuthReactorUtils.tokenTypeHandler(tokenType));
        }
        if (loginCheck) {
            chain = chain.flatMap(AuthReactorUtils.hasLoginHandler());
        }
        if (fn != null) {
            chain = chain.flatMap(fn);
        }
        if (roles != null) {
            chain = chain
                .cast(RoleAuthContext.class)
                .flatMap(AuthReactorUtils.hasRoleHandler(roles));
        }
        return chain.then(next);
    }
}
