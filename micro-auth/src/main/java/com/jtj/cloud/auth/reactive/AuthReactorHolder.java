package com.jtj.cloud.auth.reactive;

import com.jtj.cloud.auth.AuthContext;
import com.jtj.cloud.auth.rbac.RoleContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class AuthReactorHolder implements ApplicationContextAware {

    private static ApplicationContext application;
    private static RoleContext roleContext;

    public static Mono<ServerWebExchange> getExchange() {
        return Mono.deferContextual(ctx -> Mono.just(ctx.get(ServerWebExchange.class)));
    }

    public static Mono<AuthContext> getAuthContext() {
        return Mono.deferContextual(ctx -> Mono.just(ctx.get(AuthContext.class)));
    }

    public static Mono<RoleContext> getRoleContext() {
        if (roleContext != null) {
            return Mono.just(roleContext);
        }
        roleContext = application.getBean(RoleContext.class);
        return Mono.just(roleContext);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        application = applicationContext;
    }
}
