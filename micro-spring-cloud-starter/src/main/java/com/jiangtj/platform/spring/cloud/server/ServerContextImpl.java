package com.jiangtj.platform.spring.cloud.server;

import com.jiangtj.platform.auth.context.AbstractSimpleAuthContext;
import io.jsonwebtoken.Claims;

public class ServerContextImpl extends AbstractSimpleAuthContext {
    public ServerContextImpl(String token, Claims claims) {
        super(token, claims);
    }
}
