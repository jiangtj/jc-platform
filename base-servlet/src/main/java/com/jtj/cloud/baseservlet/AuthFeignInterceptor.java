package com.jtj.cloud.baseservlet;

import com.jtj.cloud.auth.AuthServer;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthFeignInterceptor implements RequestInterceptor {
    @Resource
    private AuthServer authServer;
    @Resource
    private HttpServletRequest request;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String headerName = authServer.getProperties().getHeaderName();
        requestTemplate.header(headerName, request.getHeader(headerName));
    }
}
