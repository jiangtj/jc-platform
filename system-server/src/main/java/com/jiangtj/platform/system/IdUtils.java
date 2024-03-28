package com.jiangtj.platform.system;


import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.ServerRequest;

import java.util.Map;
import java.util.Optional;

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
        return RequestPredicates.path(pattern).or(RequestPredicates.path(pathVarPatten));
    }

    static Long idFrom(ServerRequest request) {
        return idFromNullable(request).orElseThrow();
    }

    static Optional<Long> idFromNullable(ServerRequest request) {
        Map<String, String> pathVariables = request.pathVariables();
        if (pathVariables.containsKey("id")) {
            return Optional.of(Long.valueOf(pathVariables.get("id")));
        }

        return request.param("id")
                .map(Long::valueOf);
    }

}
