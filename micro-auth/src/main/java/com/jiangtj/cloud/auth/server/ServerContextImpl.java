package com.jiangtj.cloud.auth.server;

import com.jiangtj.cloud.auth.context.AbstractSimpleAuthContext;
import io.jsonwebtoken.Claims;

public class ServerContextImpl extends AbstractSimpleAuthContext {
    public ServerContextImpl(String token, Claims claims) {
        super(token, claims);
    }
}
