package com.jiangtj.cloud.auth.reactive;

import com.jiangtj.cloud.auth.AntPathMatcherUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Function;

public abstract class AuthReactiveWebFilter implements WebFilter {

    public List<String> getIncludePatterns() {
        return Collections.singletonList("/**");
    }

    public List<String> getExcludePatterns() {
        return Collections.emptyList();
    }

    public abstract AuthReactorHandler filter(AuthReactorHandler handler);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String path = request.getPath().value();
        if (!AntPathMatcherUtils.match(path, getIncludePatterns(), getExcludePatterns())) {
            return chain.filter(exchange);
        }

        return filter(new AuthReactorHandler()).getChain()
            .then(chain.filter(exchange));
    }

    static class DefaultAuthReactiveWebFilter extends AuthReactiveWebFilter {
        private final List<String> includePatterns;
        private final List<String> excludePatterns;
        private final Function<AuthReactorHandler, AuthReactorHandler> fn;

        public DefaultAuthReactiveWebFilter(List<String> includePatterns, List<String> excludePatterns, Function<AuthReactorHandler, AuthReactorHandler> fn) {
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
        public AuthReactorHandler filter(AuthReactorHandler handler) {
            return fn.apply(handler);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<String> includePatterns;
        private List<String> excludePatterns;
        private boolean includeActuator = false;
        private Function<AuthReactorHandler, AuthReactorHandler> fn;

        Builder() {
            this.includePatterns = Collections.singletonList("/**");
            this.excludePatterns = Collections.emptyList();
        }

        public Builder includeActuator() {
            this.includeActuator = true;
            return this;
        }

        public Builder include(String... patterns) {
            this.includePatterns = Arrays.asList(patterns);
            return this;
        }

        public Builder exclude(String... patterns) {
            this.excludePatterns = Arrays.asList(patterns);
            return this;
        }

        public Builder filter(Function<AuthReactorHandler, AuthReactorHandler> fn) {
            this.fn = fn;
            return this;
        }

        public AuthReactiveWebFilter build() {
            Objects.requireNonNull(this.fn);
            if (!includeActuator) {
                String actuatorPath = "/actuator/**";
                if (excludePatterns.isEmpty()) {
                    excludePatterns = Collections.singletonList(actuatorPath);
                } else {
                    if (!excludePatterns.contains(actuatorPath)) {
                        excludePatterns = new ArrayList<>(excludePatterns);
                        excludePatterns.add(actuatorPath);
                    }
                }
            }
            return new DefaultAuthReactiveWebFilter(includePatterns, excludePatterns, fn);
        }
    }
}
