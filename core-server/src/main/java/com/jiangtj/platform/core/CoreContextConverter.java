package com.jiangtj.platform.core;

import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.auth.context.AuthContextConverter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpRequest;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CoreContextConverter implements AuthContextConverter {

    @Override
    public AuthContext convert(@NotNull HttpRequest request) {
        String path = request.getURI().getPath();
        if(path.startsWith("/service/core-server")){
            return AuthContext.unauthorized();
        }
        return null;
    }

}
