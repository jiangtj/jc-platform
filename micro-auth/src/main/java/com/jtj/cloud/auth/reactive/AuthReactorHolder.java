package com.jtj.cloud.auth.reactive;

import com.jtj.cloud.auth.AuthHolder;
import com.jtj.cloud.auth.context.AuthContext;
import com.jtj.cloud.auth.rbac.RoleContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface AuthReactorHolder  {

    static Mono<ServerWebExchange> deferExchange() {
        return Mono.deferContextual(ctx -> Mono.just(ctx.get(ServerWebExchange.class)));
    }

    static Mono<AuthContext> deferAuthContext() {
        return Mono.deferContextual(ctx -> Mono.just(ctx.get(AuthContext.class)));
    }

    static Mono<RoleContext> deferRoleContext() {
        return Mono.just(AuthHolder.getRoleContext());
    }

}
