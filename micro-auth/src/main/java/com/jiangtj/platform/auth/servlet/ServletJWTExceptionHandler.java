package com.jiangtj.platform.auth.servlet;

import com.jiangtj.platform.auth.AuthExceptionUtils;
import com.jiangtj.platform.web.Orders;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Order(Orders.BASE_EXCEPTION_FILTER + 10)
public class ServletJWTExceptionHandler extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException ex) {
            throw AuthExceptionUtils.invalidToken(ex.getMessage(), ex);
        }
    }
}
