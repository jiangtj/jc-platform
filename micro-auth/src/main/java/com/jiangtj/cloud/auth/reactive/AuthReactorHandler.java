package com.jiangtj.cloud.auth.reactive;

import com.jiangtj.cloud.auth.context.AuthContext;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class AuthReactorHandler {

    Mono<AuthContext> chain;

    public AuthReactorHandler() {
        this.chain = AuthReactorHolder.deferAuthContext();
    }

    public AuthReactorHandler isTokenType(String type) {
        this.chain = this.chain.flatMap(AuthReactorUtils.tokenTypeHandler(type));
        return this;
    }

    public AuthReactorHandler hasLogin() {
        this.chain = this.chain.flatMap(AuthReactorUtils.hasLoginHandler());
        return this;
    }

    public AuthReactorHandler hasRole(String... roles) {
        this.chain = this.chain.flatMap(AuthReactorUtils.hasRoleHandler(roles));
        return this;
    }

    public AuthReactorHandler filter(Function<AuthContext,Mono<AuthContext>> fn) {
        this.chain = this.chain.flatMap(fn);
        return this;
    }

    public Mono<AuthContext> getChain() {
        return chain;
    }
}
