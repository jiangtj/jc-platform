package com.jiangtj.cloud.auth.servlet;

import com.jiangtj.cloud.auth.AuthRequestAttributes;
import com.jiangtj.cloud.auth.context.AuthContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Objects;

public interface AuthHolder {

    static AuthContext getAuthContext() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Objects.requireNonNull(requestAttributes);
        AuthContext context = (AuthContext) requestAttributes.getAttribute(AuthRequestAttributes.AUTH_CONTEXT_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        Objects.requireNonNull(context);
        return context;
    }

}
