package com.jiangtj.platform.auth.cloud;

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
            return AuthContext.unauthorized();
        }

        String token = headers.get(0);
        return factory.getAuthContext(token);
    }

}
