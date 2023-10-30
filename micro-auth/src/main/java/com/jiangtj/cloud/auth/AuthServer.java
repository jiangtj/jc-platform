package com.jiangtj.cloud.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PrivateJwk;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
public class AuthServer {

    @Resource
    private AuthProperties properties;
    @Resource
    private Environment environment;
    @Resource
    private AuthKeyLocator authKeyLocator;

    private PrivateJwk<PrivateKey, PublicKey, ?> jwk;

    @PostConstruct
    public void init() {
        KeyPair keyPair = Jwts.SIG.RS256.keyPair().build();
        UUID uuid = UUID.randomUUID();
        String kid = getApplicationName() + ":" + uuid;
        jwk = Jwks.builder()
            .id(kid)
            .keyPair(keyPair)
            .build();
//        AuthLoadBalancedClient client = loadBalancedClient.getIfUnique();
//        if (client != null) {
//            client.notifyUpdatePublicJwk(kid);
//        }
    }

    public String createServerToken(String target) {
        return this.builder()
            .setAuthType(TokenType.SERVER)
            .setAudience(target)
            .build();
    }

    public String createUserToken(String id, List<String> roles) {
        return this.builder()
            .setAuthType(TokenType.SYSTEM_USER)
            .setSubject(id)
            .setExtend(builder -> {
                if (!CollectionUtils.isEmpty(roles)) {
                    builder.claim("role", String.join(",", roles));
                }
                return builder;
            })
            .build();
    }

    public String createUserToken(String id, List<String> roles, String target) {
        return this.builder()
            .setAuthType(TokenType.SYSTEM_USER)
            .setSubject(id)
            .setExtend(builder -> {
                if (!CollectionUtils.isEmpty(roles)) {
                    builder.claim("role", String.join(",", roles));
                }
                builder = builder.audience().add(target).and();
                return builder;
            })
            .build();
    }

    public String createUserTokenFromClaim(Claims claims, String target) {
        JwtBuilder builder = Jwts.builder()
            .header().keyId(jwk.getId()).and()
            .claims(claims)
            .signWith(jwk.toKey())
            .audience().add(target).and()
            .claim(TokenType.KEY, TokenType.SYSTEM_USER)
            .issuer(getApplicationName())
            .issuedAt(new Date())
            .expiration(Date.from(Instant.now().plusSeconds(properties.getExpires().getSeconds())));

        String compact = builder.compact();
        return AuthRequestAttributes.TOKEN_HEADER_PREFIX + compact;
    }

    public KeyPair getKeyPair(){
        return jwk.toKeyPair().toJavaKeyPair();
    }

    public PrivateJwk<PrivateKey, PublicKey, ?> getPrivateJwk(){
        return jwk;
    }

    public AuthProperties getProperties() {
        return properties;
    }

    public Jws<Claims> verify(String token) {
        token = verifyRequest(token);
        JwtParser parser = Jwts.parser()
            .keyLocator(authKeyLocator)
            /*.keyLocator(header -> {
                String kid = String.valueOf(header.get("kid"));
                PublicJwk<PublicKey> publicJwk = pkMap.get(kid);
                if (publicJwk != null) {
                    return publicJwk.toKey();
                }
                AuthLoadBalancedClient client = loadBalancedClient.getIfUnique();
                if (client == null) {
                    return jwk.toPublicJwk().toKey();
                }
                publicJwk = client.getPublicJwk(kid);
                pkMap.put(publicJwk.getId(), publicJwk);
                return publicJwk.toKey();
            })*/
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
