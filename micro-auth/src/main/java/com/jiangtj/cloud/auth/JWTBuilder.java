package com.jiangtj.cloud.auth;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import java.security.PrivateKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

public class JWTBuilder {
    private String kid;
    private String issuer;
    private String subject;
    private String audience;
    private String type;
    private Duration expires;
    private final PrivateKey key;
    private final String headerPrefix;
    private Function<JwtBuilder, JwtBuilder> extend;

    public JWTBuilder(AuthServer ctx) {
        this.kid = ctx.getPrivateJwk().getId();
        this.issuer = ctx.getApplicationName();
        this.expires = ctx.getProperties().getExpires();
        this.key = ctx.getKeyPair().getPrivate();
        this.headerPrefix = AuthRequestAttributes.TOKEN_HEADER_PREFIX;
    }

    public String build() {
        JwtBuilder builder = Jwts.builder()
            .header().keyId(kid).and()
            .signWith(key)
            .subject(subject)
            .audience().add(audience).and()
            .claim(TokenType.KEY, type)
            .issuer(issuer)
            .issuedAt(new Date())
            .expiration(Date.from(Instant.now().plusSeconds(expires.getSeconds())));

        if (extend != null) {
            builder = extend.apply(builder);
        }

        String compact = builder.compact();
        return headerPrefix + compact;
    }

    public JWTBuilder setIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public JWTBuilder setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public JWTBuilder setAudience(String audience) {
        this.audience = audience;
        return this;
    }

    public JWTBuilder setAuthType(String type) {
        this.type = type;
        return this;
    }

    public JWTBuilder setExtend(Function<JwtBuilder, JwtBuilder> extend) {
        this.extend = extend;
        return this;
    }

    public JWTBuilder setExpires(Duration expires) {
        this.expires = expires;
        return this;
    }
}
