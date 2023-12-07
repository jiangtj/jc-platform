package com.jiangtj.platform.test;

import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.auth.context.AuthContextConverter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpRequest;
import org.springframework.lang.NonNull;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class TestAuthContextConverter implements AuthContextConverter {

    @Override
    public AuthContext convert(@NonNull HttpRequest request) {
        return TestAuthContextHolder.getAuthContext();
    }
}
