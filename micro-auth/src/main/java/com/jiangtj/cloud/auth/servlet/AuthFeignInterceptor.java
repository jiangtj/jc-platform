package com.jiangtj.cloud.auth.servlet;

import com.jiangtj.cloud.auth.AuthRequestAttributes;
import com.jiangtj.cloud.auth.AuthServer;
import com.jiangtj.cloud.auth.context.AuthContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Objects;

import static com.jiangtj.cloud.auth.AuthRequestAttributes.TOKEN_HEADER_NAME;

@Slf4j
public class AuthFeignInterceptor implements RequestInterceptor {

    @Resource
    private HttpServletRequest request;
    @Resource
    private AuthServer authServer;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Objects.requireNonNull(requestAttributes);
        AuthContext authContext = (AuthContext) requestAttributes.getAttribute(AuthRequestAttributes.AUTH_CONTEXT_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        if (authContext == null || !authContext.isLogin()) {
            return;
        }
        Claims claims = authContext.claims();
        String name = requestTemplate.feignTarget().name();
        String token = authServer.createUserTokenFromClaim(claims, name);
        requestTemplate.header(TOKEN_HEADER_NAME, token);

    }
}
