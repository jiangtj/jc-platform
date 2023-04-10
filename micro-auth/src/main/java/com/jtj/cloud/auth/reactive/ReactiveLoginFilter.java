package com.jtj.cloud.auth.reactive;

import com.jtj.cloud.auth.AuthProperties;
import com.jtj.cloud.auth.AuthServer;
import com.jtj.cloud.common.BaseExceptionUtils;
import jakarta.annotation.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReactiveLoginFilter implements WebFilter {
    private final List<String> withPatterns;
    private final List<String> withoutPatterns;

    @Resource
    private AuthServer authServer;

    private final AntPathMatcher matcher = new AntPathMatcher();

    public ReactiveLoginFilter(List<String> withPatterns, List<String> withoutPatterns) {
        this.withPatterns = withPatterns;
        this.withoutPatterns = withoutPatterns;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (request.getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        String path = request.getPath().value();
        AuthProperties properties = authServer.getProperties();

        if (CollectionUtils.isEmpty(withPatterns)) {
            return chain.filter(exchange);
        }
        for (String ex: withPatterns) {
            if (!matcher.match(ex, path)) {
                return chain.filter(exchange);
            }
        }
        if (!CollectionUtils.isEmpty(withoutPatterns)) {
            for (String ex: withoutPatterns) {
                if (matcher.match(ex, path)) {
                    return chain.filter(exchange);
                }
            }
        }

        List<String> headers = request.getHeaders().get(properties.getHeaderName());
        if (headers == null || headers.size() != 1) {
            throw BaseExceptionUtils.unauthorized("缺少认证信息，请在header中携带token");
        }

        return chain.filter(exchange);
    }

    public static class builder {
        private List<String> withPatterns;
        private List<String> withoutPatterns;

        public builder() {
            this.withPatterns = Collections.singletonList("/**");
            this.withoutPatterns = Collections.emptyList();
        }

        public builder with(String... patterns) {
            this.withPatterns = Arrays.asList(patterns);
            return this;
        }

        public builder without(String... patterns) {
            this.withoutPatterns = Arrays.asList(patterns);
            return this;
        }

        public ReactiveLoginFilter build() {
            return new ReactiveLoginFilter(withPatterns, withoutPatterns);
        }
    }
}
