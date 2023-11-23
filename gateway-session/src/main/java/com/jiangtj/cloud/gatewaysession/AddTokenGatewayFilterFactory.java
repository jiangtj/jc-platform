package com.jiangtj.cloud.gatewaysession;

import com.jiangtj.cloud.auth.AuthServer;
import com.jiangtj.cloud.common.BaseExceptionUtils;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.jiangtj.cloud.auth.AuthRequestAttributes.TOKEN_HEADER_NAME;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

@Slf4j
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

        return (exchange, chain) -> exchange.getSession()
            .flatMap(webSession -> {
                Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
                if (route == null) {
                    log.warn("Not found route defined!");
                    return chain.filter(exchange);
                }
                String scheme = route.getUri().getScheme();
                if (!"lb".equals(scheme)) {
                    log.warn("AddToken filter only apply in lb scheme!");
                    return chain.filter(exchange);
                }
                SystemUser admin = webSession.getAttribute("admin");
                if (admin == null) {
                    return Mono.error(BaseExceptionUtils.unauthorized("未登录"));
                }
                String roleStr = (String) webSession.getAttributes().getOrDefault("admin-role", null);
                List<String> roles = new ArrayList<>();
                if (roleStr != null) {
                    roles = Arrays.stream(roleStr.split(",")).toList();
                }
                String host = route.getUri().getHost();
                String userToken = authServer.createUserToken(String.valueOf(admin.getId()), roles, host);
                ServerHttpRequest req = exchange.getRequest()
                    .mutate()
                    .header(TOKEN_HEADER_NAME, userToken)
                    .build();
                return chain.filter(exchange.mutate().request(req).build());
            });
    }

    @Data
    public static class Config {
    }
}
