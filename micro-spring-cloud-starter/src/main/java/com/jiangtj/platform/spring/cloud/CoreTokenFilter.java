package com.jiangtj.platform.spring.cloud;

import com.jiangtj.platform.auth.AuthRequestAttributes;
import com.jiangtj.platform.spring.boot.servlet.URIUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Order(-200)
public class CoreTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ServletServerHttpRequest request1 = new ServletServerHttpRequest(request);
        if (request1.getMethod() == HttpMethod.OPTIONS) {
            filterChain.doFilter(request, response);
            return;
        }
        String path = URIUtils.getPath(request);
        if (path.startsWith("/service/core-server")) {
            request1.getHeaders().remove(AuthRequestAttributes.TOKEN_HEADER_NAME);
            filterChain.doFilter(request1.getServletRequest(), response);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
