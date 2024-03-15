package com.jiangtj.platform.spring.cloud.core;

import com.jiangtj.platform.spring.cloud.AuthServer;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Objects;

import static com.jiangtj.platform.auth.AuthRequestAttributes.TOKEN_HEADER_NAME;

public class CoreTokenInterceptor implements ClientHttpRequestInterceptor {

    @Resource
    private ObjectProvider<AuthServer> authServers;

    public String createToken() {
        return Objects.requireNonNull(authServers.getIfUnique())
            .createServerToken("core-server");
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add(TOKEN_HEADER_NAME, createToken());
        return execution.execute(request, body);
    }
}
