package com.jtj.cloud.auth.servlet;

import com.jtj.cloud.auth.AuthExceptionUtils;
import com.jtj.cloud.auth.AuthProperties;
import com.jtj.cloud.auth.AuthServer;
import com.jtj.cloud.auth.TokenType;
import com.jtj.cloud.common.BaseExceptionUtils;
import com.jtj.cloud.common.servlet.BaseExceptionFilter;
import com.jtj.cloud.common.servlet.URIUtils;
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
@Order(BaseExceptionFilter.ORDER + 20)
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

        String path = URIUtils.getPath(request);
        List<String> excludePatterns = properties.getExcludePatterns();
        if (!CollectionUtils.isEmpty(excludePatterns)) {
            for (String ex: excludePatterns) {
                if (matcher.match(ex, path)) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        }

        String header = request.getHeader(properties.getHeaderName());
        if (header == null) {
            throw BaseExceptionUtils.unauthorized("缺少认证信息，请在header中携带token");
        }
        Claims body = authServer.verifier().verify(header).getBody();

        TokenType type = TokenType.from(body);
        if (TokenType.SERVER.equals(type)) {
            if (!authServer.getApplicationName().equals(body.getAudience())) {
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
