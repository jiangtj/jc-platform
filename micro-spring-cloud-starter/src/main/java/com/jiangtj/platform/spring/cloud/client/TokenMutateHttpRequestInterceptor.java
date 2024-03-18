package com.jiangtj.platform.spring.cloud.client;

import com.jiangtj.platform.auth.AuthRequestAttributes;
import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.auth.servlet.AuthHolder;
import com.jiangtj.platform.spring.cloud.jwt.JwtAuthContext;
import jakarta.annotation.Resource;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class TokenMutateHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    @Resource
    private TokenMutateService tokenMutateService;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        AuthContext ctx = AuthHolder.getAuthContext();
        if (!ctx.isLogin()) {
            return execution.execute(request, body);
        }
        String token = tokenMutateService.mutate((JwtAuthContext) ctx, request.getURI().getHost());
        request.getHeaders().add(AuthRequestAttributes.TOKEN_HEADER_NAME, token);
        return execution.execute(request, body);
    }
}
