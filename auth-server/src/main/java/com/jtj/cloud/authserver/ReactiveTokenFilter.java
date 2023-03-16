package com.jtj.cloud.authserver;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
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
            log.error("访问路径：" + path);
            throw new UnsupportedJwtException("出错咯");
        }
        String header = headers.get(0);
        Claims body = authServer.verifier().verify(header).getBody();

        TokenType type = TokenType.from(body);
        if (TokenType.SERVER.equals(type)) {
            Assert.isTrue(authServer.getApplicationName().equals(body.getAudience()), "token error!");
            return chain.filter(exchange);
        }

        exchange.getAttributes().put("user-claims", body);
        return chain.filter(exchange);
    }
}
