package com.jiangtj.cloud.baseservlet;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.jiangtj.cloud.auth.AuthRequestAttributes.TOKEN_HEADER_NAME;

@Slf4j
@Component
public class AuthFeignInterceptor implements RequestInterceptor {

    @Resource
    private HttpServletRequest request;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String headerName = TOKEN_HEADER_NAME;
        requestTemplate.header(headerName, request.getHeader(headerName));
        String name = requestTemplate.feignTarget().name();
        log.warn("create an new token with feignTarget: {} ?", name);
        log.warn("feignTarget is experimental now");
    }
}
