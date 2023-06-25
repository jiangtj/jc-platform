package com.jiangtj.cloud.auth;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;

import javax.crypto.SecretKey;
import java.util.List;

@Slf4j
public class AuthServer {

    @Resource
    private AuthProperties properties;
    @Resource
    private Environment environment;

    private static SecretKey secretKey = null;

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

    public SecretKey getKey(){
        if (secretKey != null) {
            return secretKey;
        }
        String secret = properties.getSecret();
        if (secret == null) {
            log.warn("您未设置Key，请在配置中心设置统一的 auth.secret");
            log.warn("正在为您生成一个随机的 HS256 Key（这会导致服务间无法调用）");
            secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            return secretKey;
        }
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        return secretKey;
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
