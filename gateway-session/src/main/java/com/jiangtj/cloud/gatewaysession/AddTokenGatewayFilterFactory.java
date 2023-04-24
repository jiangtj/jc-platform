package com.jiangtj.cloud.gatewaysession;

import com.jiangtj.cloud.auth.AuthProperties;
import com.jiangtj.cloud.auth.AuthServer;
import com.jiangtj.cloud.auth.UserClaims;
import com.jiangtj.cloud.common.BaseExceptionUtils;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.jiangtj.cloud.auth.RequestAttributes.TOKEN_HEADER_NAME;

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
            .map(sub -> authServer.createUserToken(sub))
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
