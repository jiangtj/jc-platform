package com.jtj.cloud.auth;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

/**
 * Created At 2021/3/30.
 */
public class JWTBuilder {
    private String issuer;
    private String subject;
    private String audience;
    private TokenType type;
    private Duration expires;
    private final SecretKey key;
    private final String headerPrefix;
    private Function<JwtBuilder, JwtBuilder> extend;

    public JWTBuilder(AuthServer ctx) {
        this.issuer = ctx.getApplicationName();
        this.expires = ctx.getProperties().getExpires();
        this.key = ctx.getKey();
        this.headerPrefix = RequestAttributes.TOKEN_HEADER_PREFIX;
    }

    public String build() {
        JwtBuilder builder = Jwts.builder()
            .signWith(key)
            .setIssuer(issuer)
            .setSubject(subject)
            .setIssuedAt(new Date())
            .setAudience(audience)
            .claim(TokenProperties.TYPE, type.toString())
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

    public JWTBuilder setAuthType(TokenType type) {
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
