package com.jiangtj.platform.spring.cloud.jwt;

import com.jiangtj.platform.auth.AuthRequestAttributes;
import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.auth.context.AuthContextConverter;
import jakarta.annotation.Resource;
import org.springframework.http.HttpRequest;

import java.util.List;

public class MicroAuthContextConverter implements AuthContextConverter {

    @Resource
    private JwtAuthContextFactory factory;

    @Override
    public AuthContext convert(HttpRequest request) {
        List<String> headers = request.getHeaders().get(AuthRequestAttributes.TOKEN_HEADER_NAME);
        if (headers == null || headers.size() != 1) {
            return AuthContext.unLogin();
        }

        String token = headers.get(0);
        return factory.getAuthContext(token);
    }

}
