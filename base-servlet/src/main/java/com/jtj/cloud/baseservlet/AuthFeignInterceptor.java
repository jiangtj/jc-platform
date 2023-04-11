package com.jtj.cloud.baseservlet;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import static com.jtj.cloud.auth.RequestAttributes.TOKEN_HEADER_NAME;

@Component
public class AuthFeignInterceptor implements RequestInterceptor {

    @Resource
    private HttpServletRequest request;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String headerName = TOKEN_HEADER_NAME;
        requestTemplate.header(headerName, request.getHeader(headerName));
    }
}
