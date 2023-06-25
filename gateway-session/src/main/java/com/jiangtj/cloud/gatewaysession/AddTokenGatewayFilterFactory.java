package com.jiangtj.cloud.gatewaysession;

import com.jiangtj.cloud.auth.AuthProperties;
import com.jiangtj.cloud.auth.AuthServer;
import com.jiangtj.cloud.common.BaseExceptionUtils;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                SystemUser admin = (SystemUser) webSession.getAttributes().getOrDefault("admin", null);
                if (admin == null) {
                    return Mono.error(BaseExceptionUtils.unauthorized("未登录"));
                }
                String roleStr = (String) webSession.getAttributes().getOrDefault("admin-role", null);
                List<String> roles = new ArrayList<>();
                if (roleStr != null) {
                    roles = Arrays.stream(roleStr.split(",")).toList();
                }
                return Mono.just(authServer.createUserToken(String.valueOf(admin.getId()), roles));
            })
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
