package com.jtj.cloud.auth.servlet;

import com.jtj.cloud.auth.AuthProperties;
import com.jtj.cloud.auth.AuthServer;
import com.jtj.cloud.common.BaseExceptionUtils;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;


/**
 * 验证TOKEN
 * Created by maokefeng on 2017/3/28.
 */
public class ServletTokenInterceptor implements HandlerInterceptor {

    @Resource
    private AuthServer authServer;

    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if ("options".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        AuthProperties properties = authServer.getProperties();
        String path = request.getServletPath();

        List<String> excludePatterns = properties.getExcludePatterns();
        if (!CollectionUtils.isEmpty(excludePatterns)) {
            for (String ex: excludePatterns) {
                if (matcher.match(ex, path)) {
                    return true;
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
        return true;
    }

}
