package com.jtj.cloud.common.reactive;

import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;
import java.util.Optional;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;

public interface IdUtils {


    static RequestPredicate idPath() {
        return idPath("");
    }

    static RequestPredicate idPath(String pattern) {
        String pathVarPatten = pattern;
        if (!pattern.endsWith("/")) {
            pathVarPatten += "/";
        }
        pathVarPatten += "{id}";
        return path(pattern).or(path(pathVarPatten));
    }

    static Long idFrom(ServerRequest request) {
        return idFromNullable(request).orElseThrow();
    }

    static Optional<Long> idFromNullable(ServerRequest request) {
        Map<String, String> pathVariables = request.pathVariables();
        if (pathVariables.containsKey("id")) {
            return Optional.of(Long.valueOf(pathVariables.get("id")));
        }

        return request.queryParam("id")
                .map(Long::valueOf);
    }

}
