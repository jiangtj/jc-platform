package com.jtj.cloud.sql.reactive;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.reactive.function.server.ServerRequest;

public interface PageUtils {

    static PageRequest from(ServerRequest request) {
        int page = request.queryParam("page")
                .map(Integer::parseInt)
                .orElse(0);
        int size = request.queryParam("size")
                .map(Integer::parseInt)
                .orElse(10);
        String sort = request.queryParam("sort").orElse("id,DESC");
        String[] split = sort.split(",");
        Sort.Direction direction = split.length == 2?
                Sort.Direction.fromString(split[1]):
                Sort.Direction.ASC;
        return PageRequest.of(page, size, direction, split[0]);
    }

}
