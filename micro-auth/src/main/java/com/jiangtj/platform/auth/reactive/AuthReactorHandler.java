package com.jiangtj.platform.auth.reactive;

import com.jiangtj.platform.auth.context.AuthContext;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class AuthReactorHandler {

    private String tokenType;
    private boolean loginCheck = false;
    private String[] roles;
    private Function<AuthContext, Mono<AuthContext>> fn;

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

    public AuthReactorHandler filter(Function<AuthContext, Mono<AuthContext>> fn) {
        this.fn = fn;
        return this;
    }

    public <V> Mono<V> next(Mono<V> next) {
        Mono<AuthContext> chain = AuthReactorHolder.deferAuthContext();
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
            chain = chain.flatMap(AuthReactorUtils.hasRoleHandler(roles));
        }
        return chain.then(next);
    }
}
