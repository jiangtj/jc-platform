package com.jtj.cloud.authserver;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.KeyException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import javax.crypto.SecretKey;

/**
 * 2018/9/26.
 */
@Slf4j
public class AuthServer {

    @Resource
    private AuthProperties properties;
    @Resource
    private Environment environment;

    public SecretKey getKey(){
        String secret = properties.getSecret();
        if (secret == null) {
            log.warn("你未设置Key，需要设置auth.def.secret");
            throw new KeyException("Unknown key!");
        }
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public AuthProperties getProperties() {
        return properties;
    }

    public JWTVerifier verifier() {
        return new JWTVerifier(this);
    }

    public JWTBuilder builder() {
        return new JWTBuilder(this);
    }

    public String getApplicationName() {
        String applicationName = environment.getProperty("spring.application.name");
        if (applicationName != null) {
            return applicationName.toLowerCase();
        }
        return "unknown";
    }
}
