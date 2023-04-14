package com.jtj.cloud.gatewaysession;

import com.jtj.cloud.auth.AuthProperties;
import com.jtj.cloud.auth.AuthServer;
import com.jtj.cloud.auth.TokenType;
import com.jtj.cloud.auth.UserClaims;
import com.jtj.cloud.common.BaseExceptionUtils;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.jtj.cloud.auth.RequestAttributes.TOKEN_HEADER_NAME;

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
                Object admin = webSession.getAttributes().getOrDefault("admin-claims", null);
                if (admin == null) {
                    return Mono.error(BaseExceptionUtils.unauthorized("未登录"));
                }
                return Mono.just(admin);
            })
            .cast(UserClaims.class)
            .map(sub -> authServer.builder()
                .setAuthType(TokenType.SYSTEM)
                .setSubject(sub.id())
                .setExtend(builder -> {
                    List<String> roles = sub.roles();
                    if (!CollectionUtils.isEmpty(roles)) {
                        builder.claim("role", String.join(",", roles));
                    }
                    return builder;
                })
                .build())
            .map(token -> exchange.getRequest()
                .mutate()
                .header(TOKEN_HEADER_NAME, token)
                .build())
            .map(req -> exchange.mutate().request(req).build())
            .then(chain.filter(exchange));
    }

    @Data
    static class Config {
    }
}
