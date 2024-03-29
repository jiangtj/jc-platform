package com.jiangtj.platform.spring.cloud;

import com.jiangtj.platform.auth.AuthRequestAttributes;
import com.jiangtj.platform.spring.cloud.jwt.AuthKeyLocator;
import com.jiangtj.platform.web.ApplicationProperty;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.PrivateJwk;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Slf4j
public class AuthServer {

    @Resource
    private AuthProperties properties;
    @Resource
    private ApplicationProperty applicationProperty;
    @Resource
    private AuthKeyLocator authKeyLocator;
    @Resource
    private PublicKeyCachedService publicKeyCachedService;

    @PostConstruct
    public void init() {
        JwkHolder.init(properties, getApplicationName());
        PublicJwk<PublicKey> publicJwk = JwkHolder.getPublicJwk();
        if (publicJwk != null) {
            publicKeyCachedService.setPublicJwk(publicJwk);
        }
    }

    public JwtBuilder builder() {
        PrivateJwk<PrivateKey, PublicKey, ?> privateJwk = JwkHolder.getPrivateJwk();
        return Jwts.builder()
            .header().keyId(privateJwk.getId()).and()
            .signWith(privateJwk.toKey())
            .issuer(getApplicationName())
            .issuedAt(new Date())
            .expiration(Date.from(Instant.now().plusSeconds(properties.getExpires().getSeconds())));
    }

    public String toToken(JwtBuilder builder) {
        String compact = builder.compact();
        return AuthRequestAttributes.TOKEN_HEADER_PREFIX + compact;
    }

    public String createServerToken(String target) {
        JwtBuilder builder = builder()
            .claim(Providers.KEY, Providers.SERVER)
            .audience().add(target).and();
        return toToken(builder);
    }

    public String createUserToken(String id, List<String> roles, String target) {
        JwtBuilder builder = builder()
            .claim(Providers.KEY, Providers.SYSTEM_USER)
            .audience().add(target).and()
            .subject(id);

        if (!CollectionUtils.isEmpty(roles)) {
            builder.claim("role", String.join(",", roles));
        }

        return toToken(builder);
    }

    public String createTokenFromClaim(Claims claims, String target) {
        PrivateJwk<PrivateKey, PublicKey, ?> privateJwk = JwkHolder.getPrivateJwk();
        JwtBuilder builder = Jwts.builder()
            .header().keyId(privateJwk.getId()).and()
            .claims(claims)
            .signWith(privateJwk.toKey())
            .audience().add(target).and()
            .issuer(getApplicationName())
            .issuedAt(new Date())
            .expiration(Date.from(Instant.now().plusSeconds(properties.getExpires().getSeconds())));
        return toToken(builder);
    }

    public PrivateJwk<PrivateKey, PublicKey, ?> getPrivateJwk(){
        return JwkHolder.getPrivateJwk();
    }

    public Jws<Claims> verify(String token) {
        token = verifyRequest(token);
        JwtParser parser = Jwts.parser()
            .keyLocator(authKeyLocator)
            .build();
        Jws<Claims> claims = parser.parseSignedClaims(token);
        verifyTime(claims.getPayload());
        return claims;
    }

    private String verifyRequest(String token) {
        String headerPrefix = AuthRequestAttributes.TOKEN_HEADER_PREFIX;
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
        Duration timeout = Duration.between(issuedAt.toInstant(), expiration.toInstant());
        if (timeout.compareTo(properties.getMaxExpires()) > 0) {
            log.warn("Timeout is bigger than max expires time!");
            throw new ExpiredJwtException(null, body, "ExpiresTime is bigger than max expires time!");
        }
    }

    // use applicationProperty.getName()
    @Deprecated
    public String getApplicationName() {
        String applicationName = applicationProperty.getName();
        if (applicationName != null) {
            return applicationName;
        }
        return "unknown";
    }
}
