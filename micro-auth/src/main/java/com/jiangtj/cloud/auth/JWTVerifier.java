package com.jiangtj.cloud.auth;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Slf4j
public class JWTVerifier {

    private final Duration maxExpires;
    private final SecretKey key;
    private final String headerPrefix;

    public JWTVerifier(AuthServer ctx) {
        this.maxExpires = ctx.getProperties().getMaxExpires();
        this.key = ctx.getKey();
        this.headerPrefix = AuthRequestAttributes.TOKEN_HEADER_PREFIX;
    }

    public Jws<Claims> verify(String token) {
        token = verifyRequest(token);
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
        Jws<Claims> claims = parser.parseClaimsJws(token);
        verifyTime(claims.getBody());
        return claims;
    }

    public String verifyRequest(String token) {
        if (!StringUtils.hasLength(token)) {
            log.warn("Token is empty!");
            throw new UnsupportedJwtException("Token is empty!");
        }
        if (!token.startsWith(headerPrefix)) {
            log.warn("Don't have prefix {}!", headerPrefix);
            throw new UnsupportedJwtException("Unsupported authu jwt token!");
        }
        return token.substring(headerPrefix.length());
    }

    public void verifyTime(Claims body) {
        Date issuedAt = body.getIssuedAt();
        Date expiration = body.getExpiration();
        if (issuedAt == null || expiration == null) {
            log.warn("IssuedAt or Expiration is empty!");
            throw new RequiredTypeException("IssuedAt or Expiration is empty!");
        }
        Duration timeout = Duration.between(issuedAt.toInstant(), expiration.toInstant());
        if (timeout.compareTo(maxExpires) > 0) {
            log.warn("Timeout is bigger than max expires time!");
            throw new ExpiredJwtException(null, body, "ExpiresTime is bigger than max expires time!");
        }
    }
}
