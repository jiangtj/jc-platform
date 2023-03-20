package com.jtj.cloud.auth.servlet;

import com.jtj.cloud.auth.AuthProperties;
import com.jtj.cloud.auth.AuthServer;
import com.jtj.cloud.common.BaseExceptionUtils;
import com.jtj.cloud.common.servlet.BaseExceptionFilter;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


/**
 * 验证TOKEN
 * Created by maokefeng on 2017/3/28.
 */
@Order(BaseExceptionFilter.ORDER + 1)
public class ServletTokenFilter extends OncePerRequestFilter {

    @Resource
    private AuthServer authServer;

    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("options".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        AuthProperties properties = authServer.getProperties();
        String pathInfo = request.getPathInfo();
        String servletPath = request.getServletPath();

        List<String> excludePatterns = properties.getExcludePatterns();
        if (!CollectionUtils.isEmpty(excludePatterns)) {
            for (String ex: excludePatterns) {
                if (matcher.match(ex, servletPath)) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        }

        String header = request.getHeader(properties.getHeaderName());
        if (header == null) {
            throw BaseExceptionUtils.invalidToken("缺少有效的 token！");
        }
        Claims claims = authServer.verifier().verify(header).getBody();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            requestAttributes.setAttribute("user-claims", claims, RequestAttributes.SCOPE_REQUEST);
        }
        filterChain.doFilter(request, response);
    }
}
