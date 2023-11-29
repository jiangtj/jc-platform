package com.jiangtj.platform.auth.cloud.server;

import com.jiangtj.platform.auth.context.AbstractSimpleAuthContext;
import io.jsonwebtoken.Claims;

public class ServerContextImpl extends AbstractSimpleAuthContext {
    public ServerContextImpl(String token, Claims claims) {
        super(token, claims);
    }
}
