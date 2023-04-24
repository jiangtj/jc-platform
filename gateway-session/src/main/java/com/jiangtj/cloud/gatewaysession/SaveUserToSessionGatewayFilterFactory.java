package com.jiangtj.cloud.gatewaysession;

import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Deprecated
@Component
public class SaveUserToSessionGatewayFilterFactory extends AbstractGatewayFilterFactory<SaveUserToSessionGatewayFilterFactory.Config> {

    @Resource
    private ModifyResponseBodyGatewayFilterFactory modifyResponseBodyGatewayFilterFactory;

    public SaveUserToSessionGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public String name() {
        return "SaveUserToSession";
    }

    /*@Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> chain.filter(exchange)
            .then(modifyResponseBodyGatewayFilterFactory
                .apply(c -> c.setRewriteFunction(SystemUser.class, SystemUser.class,
                    (exchange1, user) -> exchange.getSession()
                        .flatMap(webSession -> {
                            webSession.getAttributes().put("admin", user.getId());
                            return Mono.just(user);
                        })))
                .filter(exchange, chain));
    }*/

    @Override
    public GatewayFilter apply(Config config) {
        return modifyResponseBodyGatewayFilterFactory
            .apply(c -> c.setRewriteFunction(SystemUser.class, SystemUser.class,
                (exchange, user) -> exchange.getSession()
                    .flatMap(webSession -> {
                        webSession.getAttributes().put("admin", user.getId());
                        return Mono.just(user);
                    })));
    }

    @Data
    static class Config {
    }
}
