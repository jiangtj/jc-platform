package com.jiangtj.platform.auth.servlet;

import com.jiangtj.platform.auth.AntPathMatcherUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public abstract class AuthOncePerRequestFilter extends OncePerRequestFilter {

    public List<String> getIncludePatterns() {
        return Collections.singletonList("/**");
    }

    public List<String> getExcludePatterns() {
        return Collections.emptyList();
    }


    public abstract void filter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = URIUtils.getPath(request);
        if (!AntPathMatcherUtils.match(path, getIncludePatterns(), getExcludePatterns())) {
            filterChain.doFilter(request, response);
            return;
        }

        filter(request, response, filterChain);
    }
}
