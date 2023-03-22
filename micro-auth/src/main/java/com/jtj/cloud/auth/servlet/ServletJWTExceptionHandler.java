package com.jtj.cloud.auth.servlet;

import com.jtj.cloud.auth.AuthExceptionUtils;
import com.jtj.cloud.common.servlet.BaseExceptionFilter;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
@Order(BaseExceptionFilter.ORDER + 10)
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
