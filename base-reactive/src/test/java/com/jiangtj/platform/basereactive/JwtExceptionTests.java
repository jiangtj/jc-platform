package com.jiangtj.platform.basereactive;

import com.jiangtj.platform.auth.AuthServer;
import com.jiangtj.platform.auth.TokenType;
import com.jiangtj.platform.test.JCloudWebClientBuilder;
import com.jiangtj.platform.test.JCloudWebTest;
import io.jsonwebtoken.JwtBuilder;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static com.jiangtj.platform.auth.AuthRequestAttributes.TOKEN_HEADER_NAME;

@JCloudWebTest
class JwtExceptionTests {

    @Resource
    AuthServer authServer;

    @Test
    void testJwtException(JCloudWebClientBuilder client) {
        JwtBuilder builder = authServer.builder()
            .claim(TokenType.KEY, TokenType.SERVER)
            .expiration(Date.from(Instant.now().plus(Duration.ofMinutes(50))));
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        detail.setTitle("Invalid Token");
        detail.setDetail("ExpiresTime is bigger than max expires time!");
        detail.setInstance(URI.create("/needyoken"));
        client.build().get().uri("/needyoken")
            .header(TOKEN_HEADER_NAME, authServer.toToken(builder))
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody(ProblemDetail.class).isEqualTo(detail);
    }
}
