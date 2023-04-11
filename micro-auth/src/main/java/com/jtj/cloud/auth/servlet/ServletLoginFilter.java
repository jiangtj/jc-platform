package com.jtj.cloud.auth.servlet;

import com.jtj.cloud.auth.AuthProperties;
import com.jtj.cloud.auth.AuthServer;
import com.jtj.cloud.auth.RequestAttributes;
import com.jtj.cloud.common.BaseExceptionUtils;
import com.jtj.cloud.common.servlet.BaseExceptionFilter;
import com.jtj.cloud.common.servlet.URIUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * 验证TOKEN
 * Created by maokefeng on 2017/3/28.
 */
@Order(BaseExceptionFilter.ORDER + 20)
public class ServletLoginFilter extends OncePerRequestFilter {

    private final List<String> withPatterns;
    private final List<String> withoutPatterns;

    @Resource
    private AuthServer authServer;

    private final AntPathMatcher matcher = new AntPathMatcher();

    public ServletLoginFilter(List<String> withPatterns, List<String> withoutPatterns) {
        this.withPatterns = withPatterns;
        this.withoutPatterns = withoutPatterns;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("options".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        AuthProperties properties = authServer.getProperties();

        String path = URIUtils.getPath(request);

        if (CollectionUtils.isEmpty(withPatterns)) {
            filterChain.doFilter(request, response);
            return;
        }
        for (String ex: withPatterns) {
            if (!matcher.match(ex, path)) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        if (!CollectionUtils.isEmpty(withoutPatterns)) {
            for (String ex: withoutPatterns) {
                if (matcher.match(ex, path)) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        }

        String header = request.getHeader(RequestAttributes.TOKEN_HEADER_NAME);
        if (header == null) {
            throw BaseExceptionUtils.unauthorized("缺少认证信息，请在header中携带token");
        }

        filterChain.doFilter(request, response);
    }

    public static class builder {
        private List<String> withPatterns;
        private List<String> withoutPatterns;

        public builder() {
            this.withPatterns = Collections.singletonList("/**");
            this.withoutPatterns = Collections.emptyList();
        }

        public builder with(String... patterns) {
            this.withPatterns = Arrays.asList(patterns);
            return this;
        }

        public builder without(String... patterns) {
            this.withoutPatterns = Arrays.asList(patterns);
            return this;
        }

        public ServletLoginFilter build() {
            return new ServletLoginFilter(withPatterns, withoutPatterns);
        }
    }
}
