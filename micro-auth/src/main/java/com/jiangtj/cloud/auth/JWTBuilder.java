package com.jiangtj.cloud.auth;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

public class JWTBuilder {
    private String issuer;
    private String subject;
    private String audience;
    private String type;
    private Duration expires;
    private final SecretKey key;
    private final String headerPrefix;
    private Function<JwtBuilder, JwtBuilder> extend;

    public JWTBuilder(AuthServer ctx) {
        this.issuer = ctx.getApplicationName();
        this.expires = ctx.getProperties().getExpires();
        this.key = ctx.getKey();
        this.headerPrefix = AuthRequestAttributes.TOKEN_HEADER_PREFIX;
    }

    public String build() {
        JwtBuilder builder = Jwts.builder()
            .signWith(key)
            .setIssuer(issuer)
            .setSubject(subject)
            .setIssuedAt(new Date())
            .setAudience(audience)
            .claim(TokenType.KEY, type)
            .setExpiration(Date.from(Instant.now().plusSeconds(expires.getSeconds())));

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
