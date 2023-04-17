package com.jtj.cloud.auth.reactive;

import com.jtj.cloud.auth.AntPathMatcherUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public abstract class AuthReactiveWebFilter implements WebFilter {
    public final AuthReactorHandler handler = new AuthReactorHandler();

    public List<String> getIncludePatterns() {
        return Collections.singletonList("/**");
    }

    public List<String> getExcludePatterns() {
        return Collections.emptyList();
    }

    public abstract Mono<Void> filter(AuthReactorHandler handler);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String path = request.getPath().value();
        if (!AntPathMatcherUtils.match(path, getIncludePatterns(), getExcludePatterns())) {
            return chain.filter(exchange);
        }

        return filter(handler).then(chain.filter(exchange));
    }

    static class DefaultAuthReactiveWebFilter extends AuthReactiveWebFilter {
        private final List<String> includePatterns;
        private final List<String> excludePatterns;
        private final Function<AuthReactorHandler, Mono<Void>> fn;

        public DefaultAuthReactiveWebFilter(List<String> includePatterns, List<String> excludePatterns, Function<AuthReactorHandler, Mono<Void>> fn) {
            this.includePatterns = includePatterns;
            this.excludePatterns = excludePatterns;
            this.fn = fn;
        }

        @Override
        public List<String> getIncludePatterns() {
            return includePatterns;
        }

        @Override
        public List<String> getExcludePatterns() {
            return excludePatterns;
        }

        @Override
        public Mono<Void> filter(AuthReactorHandler handler) {
            return fn.apply(handler);
        }
    }

    public static class builder {
        private List<String> includePatterns;
        private List<String> excludePatterns;
        private Function<AuthReactorHandler, Mono<Void>> fn;

        public builder() {
            this.includePatterns = Collections.singletonList("/**");
            this.excludePatterns = Collections.emptyList();
        }

        public builder include(String... patterns) {
            this.includePatterns = Arrays.asList(patterns);
            return this;
        }

        public builder exclude(String... patterns) {
            this.excludePatterns = Arrays.asList(patterns);
            return this;
        }

        public builder filter(Function<AuthReactorHandler, Mono<Void>> fn) {
            this.fn = fn;
            return this;
        }

        public AuthReactiveWebFilter build() {
            Objects.requireNonNull(this.fn);
            return new DefaultAuthReactiveWebFilter(includePatterns, excludePatterns, fn);
        }
    }
}
