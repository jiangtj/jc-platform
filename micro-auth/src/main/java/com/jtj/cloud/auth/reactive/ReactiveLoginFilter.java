package com.jtj.cloud.auth.reactive;

import com.jtj.cloud.auth.AntPathMatcherUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.jtj.cloud.auth.reactive.ReactiveTokenFilter.ORDER;

@Order(ORDER + 10)
public class ReactiveLoginFilter implements WebFilter {
    private final List<String> withPatterns;
    private final List<String> withoutPatterns;

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
        if (!AntPathMatcherUtils.match(path, withPatterns, withoutPatterns)) {
            return chain.filter(exchange);
        }

        return AuthReactorUtils.hasLogin()
            .then(chain.filter(exchange));

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
