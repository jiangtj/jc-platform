package com.jiangtj.platform.spring.cloud.server;

import com.jiangtj.platform.spring.cloud.jwt.AbstractSimpleAuthContext;
import io.jsonwebtoken.Claims;
import org.springframework.lang.Nullable;

import java.util.Set;

public class ServerContextImpl extends AbstractSimpleAuthContext {
    public ServerContextImpl(String token, Claims claims) {
        super(token, claims);
    }

    public Set<String> getAudience() {
        return claims().getAudience();
    }

    public boolean hasAudience(@Nullable String aud) {
        if (aud == null) {
            return false;
        }
        return getAudience().contains(aud);
    }
}
