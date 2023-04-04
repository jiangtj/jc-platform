package com.jtj.cloud.common.reactive;

import org.springframework.web.ErrorResponseException;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;

public class URIUtils {

    public static void update(ErrorResponseException ex, ServerWebExchange exchange) {
        URI uri = ex.getBody().getInstance();
        if (uri == null) {
            String path = exchange.getRequest().getPath().value();
            ex.setInstance(URI.create(path));
        }
    }

}
