package com.jiangtj.platform.system;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.function.ServerRequest;

import java.util.List;
import java.util.Optional;

public interface PageRequestUtils {

    static PageRequest from(ServerRequest request) {
        int page = request.param("page")
            .map(Integer::parseInt)
            .orElse(0);
        int size = request.param("size")
            .map(Integer::parseInt)
            .orElse(10);
        String sort = request.param("sort").orElse("id,DESC");
        String[] split = sort.split(",");
        Sort.Direction direction = split.length == 2?
            Sort.Direction.fromString(split[1]):
            Sort.Direction.ASC;
        return PageRequest.of(page, size, direction, split[0]);
    }

    static PageRequest from(ServerHttpRequest request) {
        int page = getQueryParam(request, "page")
            .map(Integer::parseInt)
            .orElse(0);
        int size = getQueryParam(request, "size")
            .map(Integer::parseInt)
            .orElse(10);
        String sort = getQueryParam(request, "sort").orElse("id,DESC");
        String[] split = sort.split(",");
        Sort.Direction direction = split.length == 2?
            Sort.Direction.fromString(split[1]):
            Sort.Direction.ASC;
        return PageRequest.of(page, size, direction, split[0]);
    }

    static Optional<String> getQueryParam(ServerHttpRequest request, String name) {
        List<String> queryParamValues = request.getQueryParams().get(name);
        if (CollectionUtils.isEmpty(queryParamValues)) {
            return Optional.empty();
        }
        else {
            String value = queryParamValues.get(0);
            if (value == null) {
                value = "";
            }
            return Optional.of(value);
        }
    }

}
