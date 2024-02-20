package com.jiangtj.platform.auth.servlet;

import com.jiangtj.platform.auth.AntPathMatcherUtils;
import com.jiangtj.platform.auth.AuthRequestAttributes;
import com.jiangtj.platform.spring.boot.BaseExceptionUtils;
import com.jiangtj.platform.spring.boot.servlet.BaseExceptionFilter;
import com.jiangtj.platform.spring.boot.servlet.URIUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Order(BaseExceptionFilter.ORDER + 20)
public class ServletLoginFilter extends OncePerRequestFilter {

    private final List<String> withPatterns;
    private final List<String> withoutPatterns;

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

        String path = URIUtils.getPath(request);
        if (!AntPathMatcherUtils.match(path, withPatterns, withoutPatterns)) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(AuthRequestAttributes.TOKEN_HEADER_NAME);
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
