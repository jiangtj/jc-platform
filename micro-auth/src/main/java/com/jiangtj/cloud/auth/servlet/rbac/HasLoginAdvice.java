package com.jiangtj.cloud.auth.servlet.rbac;

import com.jiangtj.cloud.auth.AuthExceptionUtils;
import com.jiangtj.cloud.auth.AuthRequestAttributes;
import com.jiangtj.cloud.auth.context.AuthContext;
import com.jiangtj.cloud.auth.context.AuthContextFactory;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
public class HasLoginAdvice implements MethodBeforeAdvice, Ordered {

    @Resource
    private HttpServletRequest request;
    @Resource
    private AuthContextFactory authContextFactory;

    @Override
    public void before(@NonNull Method method, @NonNull Object[] args, Object target) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Objects.requireNonNull(requestAttributes);
        AuthContext authContext = (AuthContext) requestAttributes.getAttribute(AuthRequestAttributes.AUTH_CONTEXT_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        if (authContext == null) {
            String header = request.getHeader(AuthRequestAttributes.TOKEN_HEADER_NAME);
            if (header == null) {
                log.info(request.getServletPath());
                authContext = AuthContext.unauthorized();
            } else {
                authContext = authContextFactory.getAuthContext(header);
            }
            requestAttributes.setAttribute(AuthRequestAttributes.AUTH_CONTEXT_ATTRIBUTE, authContext, RequestAttributes.SCOPE_REQUEST);
        }
        if (!authContext.isLogin()) {
            throw AuthExceptionUtils.unLogin();
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
