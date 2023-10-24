package com.jiangtj.cloud.auth.servlet;

import com.jiangtj.cloud.auth.AuthExceptionUtils;
import com.jiangtj.cloud.auth.AuthRequestAttributes;
import com.jiangtj.cloud.auth.AuthServer;
import com.jiangtj.cloud.auth.TokenType;
import com.jiangtj.cloud.common.servlet.BaseExceptionFilter;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;


@Order(BaseExceptionFilter.ORDER + 20)
public class ServletTokenFilter extends OncePerRequestFilter {

    @Resource
    private AuthServer authServer;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("options".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(AuthRequestAttributes.TOKEN_HEADER_NAME);
        if (header == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Claims body = authServer.verify(header).getPayload();
        if ("server".equals(body.get(TokenType.KEY))) {
            Set<String> audience = body.getAudience();
            if (!audience.contains(authServer.getApplicationName())) {
                throw AuthExceptionUtils.invalidToken("不支持访问当前服务", null);
            }
            filterChain.doFilter(request, response);
            return;
        }

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            requestAttributes.setAttribute("user-claims", body, RequestAttributes.SCOPE_REQUEST);
        }
        filterChain.doFilter(request, response);
    }
}
