package com.jiangtj.platform.spring.cloud.server;

import com.jiangtj.platform.auth.AuthExceptionUtils;
import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.auth.servlet.AuthHolder;
import com.jiangtj.platform.auth.servlet.AuthOncePerRequestFilter;
import com.jiangtj.platform.auth.servlet.AuthUtils;
import com.jiangtj.platform.web.ApplicationProperty;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class ServerProviderOncePerRequestFilter extends AuthOncePerRequestFilter {


    @Resource
    private ApplicationProperty applicationProperty;
    @Resource
    private ServerProviderProperties properties;

    @Override
    public List<String> getIncludePatterns() {
        return properties.getPath();
    }

    @Override
    public List<String> getExcludePatterns() {
        return properties.getExclude();
    }

    @Override
    public void filter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        AuthUtils.hasLogin();
        AuthContext ctx = AuthHolder.getAuthContext();

        if (ServerTokenUtils.check(ctx, applicationProperty.getName())) {
            filterChain.doFilter(request, response);
            return;
        }

        throw AuthExceptionUtils.invalidToken("不支持的 Auth Context", null);
    }
}
