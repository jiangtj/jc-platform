package com.jiangtj.platform.test.cloud;

import com.jiangtj.platform.auth.AuthRequestAttributes;
import com.jiangtj.platform.spring.cloud.AuthProperties;
import com.jiangtj.platform.spring.cloud.JwkHolder;
import com.jiangtj.platform.spring.cloud.Providers;
import com.jiangtj.platform.web.ApplicationProperty;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.PrivateJwk;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;

@Slf4j
public class AuthTestServer {

    @Resource
    private AuthProperties properties;
    @Resource
    private ApplicationProperty applicationProperty;

    public String toToken(JwtBuilder builder) {
        String compact = builder.compact();
        return AuthRequestAttributes.TOKEN_HEADER_PREFIX + compact;
    }

    public String createServerTokenFrom(String source) {
        PrivateJwk<PrivateKey, PublicKey, ?> privateJwk = JwkHolder.getPrivateJwk();
        JwtBuilder builder = Jwts.builder()
                .header().keyId(privateJwk.getId()).and()
                .signWith(privateJwk.toKey())
                .issuer(source)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusSeconds(properties.getExpires().getSeconds())))
                .claim(Providers.KEY, Providers.SERVER)
                .audience().add(applicationProperty.getName()).and();
        return toToken(builder);
    }

}
