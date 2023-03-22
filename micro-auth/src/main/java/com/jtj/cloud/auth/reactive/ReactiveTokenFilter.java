package com.jtj.cloud.auth.reactive;

import com.jtj.cloud.auth.AuthExceptionUtils;
import com.jtj.cloud.auth.AuthProperties;
import com.jtj.cloud.auth.AuthServer;
import com.jtj.cloud.auth.TokenType;
import com.jtj.cloud.common.BaseExceptionUtils;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

public class ReactiveTokenFilter implements WebFilter {

    @Resource
    private AuthServer authServer;

    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (request.getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        String path = request.getPath().value();
        AuthProperties properties = authServer.getProperties();

        List<String> excludePatterns = properties.getExcludePatterns();
        if (!CollectionUtils.isEmpty(excludePatterns)) {
            for (String ex: excludePatterns) {
                if (matcher.match(ex, path)) {
                    return chain.filter(exchange);
                }
            }
        }

        List<String> headers = request.getHeaders().get(properties.getHeaderName());
        if (headers == null || headers.size() != 1) {
            throw BaseExceptionUtils.unauthorized("缺少认证信息，请在header中携带token");
        }

        String header = headers.get(0);
        Claims body = authServer.verifier().verify(header).getBody();

        TokenType type = TokenType.from(body);
        if (TokenType.SERVER.equals(type)) {
            if (!authServer.getApplicationName().equals(body.getAudience())) {
                throw AuthExceptionUtils.invalidToken("不支持访问当前服务", null);
            }
            return chain.filter(exchange);
        }

        exchange.getAttributes().put("user-claims", body);
        return chain.filter(exchange);
    }
}
