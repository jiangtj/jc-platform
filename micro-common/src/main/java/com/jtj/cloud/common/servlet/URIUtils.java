package com.jtj.cloud.common.servlet;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.ErrorResponseException;

import java.net.URI;

public class URIUtils {

    public static String getPath(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        String pathInfo = request.getPathInfo();
        return pathInfo == null ? servletPath : servletPath + pathInfo;
    }

    public static void update(ErrorResponseException ex, HttpServletRequest request) {
        URI uri = ex.getBody().getInstance();
        if (uri == null) {
            String path = URIUtils.getPath(request);
            ex.setInstance(URI.create(path));
        }
    }

}
