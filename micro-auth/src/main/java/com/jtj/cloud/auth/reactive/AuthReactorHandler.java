package com.jtj.cloud.auth.reactive;

import reactor.core.publisher.Mono;

public class AuthReactorHandler {

    public Mono<Void> isTokenType(String type) {
        return AuthReactorUtils.isTokenType(type);
    }

    public Mono<Void> hasLogin() {
        return AuthReactorUtils.hasLogin();
    }

    public Mono<Void> hasRole(String... roles) {
        return AuthReactorUtils.hasRole(roles);
    }

}
