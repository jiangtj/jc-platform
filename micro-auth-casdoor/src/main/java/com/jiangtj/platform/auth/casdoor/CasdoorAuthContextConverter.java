package com.jiangtj.platform.auth.casdoor;

import com.jiangtj.platform.auth.AuthRequestAttributes;
import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.auth.context.AuthContextConverter;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import org.casbin.casdoor.entity.User;
import org.casbin.casdoor.service.AuthService;
import org.springframework.http.HttpRequest;

import java.util.List;

public class CasdoorAuthContextConverter implements AuthContextConverter {

    @Resource
    private AuthService casdoorAuthService;


    @Override
    @Nullable
    public AuthContext convert(HttpRequest request) {
        List<String> headers = request.getHeaders().get(AuthRequestAttributes.TOKEN_HEADER_NAME);
        if (headers == null || headers.size() != 1) {
            return null;
        }

        String token = headers.get(0);
        String headerPrefix = AuthRequestAttributes.TOKEN_HEADER_PREFIX;
        User user = casdoorAuthService.parseJwtToken(token.substring(headerPrefix.length()));
        return new CasdoorUserContextImpl(user);
    }

}
