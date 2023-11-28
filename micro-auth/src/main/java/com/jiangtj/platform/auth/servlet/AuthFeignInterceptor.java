package com.jiangtj.platform.auth.servlet;

import com.jiangtj.platform.auth.AuthRequestAttributes;
import com.jiangtj.platform.auth.TokenMutateService;
import com.jiangtj.platform.auth.context.AuthContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Objects;

import static com.jiangtj.platform.auth.AuthRequestAttributes.TOKEN_HEADER_NAME;

@Slf4j
public class AuthFeignInterceptor implements RequestInterceptor {

    @Resource
    private TokenMutateService tokenMutateService;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Objects.requireNonNull(requestAttributes);
        AuthContext authContext = (AuthContext) requestAttributes.getAttribute(AuthRequestAttributes.AUTH_CONTEXT_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        if (authContext == null || !authContext.isLogin()) {
            return;
        }
        String name = requestTemplate.feignTarget().name();
        String token = tokenMutateService.mutate(authContext, name);
        requestTemplate.header(TOKEN_HEADER_NAME, token);
    }
}
