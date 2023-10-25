package com.jiangtj.cloud.auth.servlet;

import com.jiangtj.cloud.auth.AuthRequestAttributes;
import com.jiangtj.cloud.auth.context.AuthContext;
import com.jiangtj.cloud.auth.context.AuthContextFactory;
import com.jiangtj.cloud.common.servlet.BaseExceptionFilter;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;


@Slf4j
@Order(BaseExceptionFilter.ORDER + 20)
public class ServletTokenFilter extends OncePerRequestFilter {

    @Resource
    private AuthContextFactory authContextFactory;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("options".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Objects.requireNonNull(requestAttributes);
        String header = request.getHeader(AuthRequestAttributes.TOKEN_HEADER_NAME);
        if (header == null) {
            log.info(request.getServletPath());
            AuthContext authContext =AuthContext.unauthorized();
            requestAttributes.setAttribute(AuthRequestAttributes.AUTH_CONTEXT_ATTRIBUTE, authContext, RequestAttributes.SCOPE_REQUEST);
            filterChain.doFilter(request, response);
            return;
        }

        AuthContext authContext = authContextFactory.getAuthContext(header);
        requestAttributes.setAttribute(AuthRequestAttributes.AUTH_CONTEXT_ATTRIBUTE, authContext, RequestAttributes.SCOPE_REQUEST);

        filterChain.doFilter(request, response);
    }
}
