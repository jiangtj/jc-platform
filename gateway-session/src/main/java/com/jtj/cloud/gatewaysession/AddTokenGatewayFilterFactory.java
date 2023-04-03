package com.jtj.cloud.gatewaysession;

import com.jtj.cloud.auth.AuthProperties;
import com.jtj.cloud.auth.AuthServer;
import com.jtj.cloud.auth.TokenType;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AddTokenGatewayFilterFactory extends AbstractGatewayFilterFactory<AddTokenGatewayFilterFactory.Config> {

    @Resource
    private AuthServer authServer;

    public AddTokenGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public String name() {
        return "AddToken";
    }

    @Override
    public GatewayFilter apply(Config config) {
        AuthProperties properties = authServer.getProperties();

        return (exchange, chain) -> exchange.getSession()
            .flatMap(webSession -> {
                Object admin = webSession.getAttributes().getOrDefault("admin", null);
                if (admin == null) {
                    return Mono.empty();
                }
                return Mono.just(admin);
            })
            .map(String::valueOf)
            .map(sub -> authServer.builder()
                .setAuthType(TokenType.SYSTEM)
                .setSubject(sub)
                .build())
            .map(token -> exchange.getRequest()
                .mutate()
                .header(properties.getHeaderName(), token)
                .build())
            .map(req -> exchange.mutate().request(req).build())
            .then(chain.filter(exchange));
    }

    @Data
    static class Config {
    }
}
