package com.jiangtj.cloud.baseservlet;

import com.jiangtj.cloud.auth.AuthServer;
import com.jiangtj.cloud.auth.TokenType;
import com.jiangtj.cloud.baseservlet.base.AbstractServerTests;
import io.jsonwebtoken.JwtBuilder;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static com.jiangtj.cloud.auth.AuthRequestAttributes.TOKEN_HEADER_NAME;

public class JwtExceptionTests extends AbstractServerTests {
    @Resource
    WebTestClient webClient;
    @Resource
    AuthServer authServer;

    @Test
    void testJwtException() {
        JwtBuilder builder = authServer.builder()
            .claim(TokenType.KEY, TokenType.SERVER)
            .expiration(Date.from(Instant.now().plus(Duration.ofMinutes(50))));
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        detail.setTitle("Invalid Token");
        detail.setDetail("ExpiresTime is bigger than max expires time!");
        detail.setInstance(URI.create("/needyoken"));
        webClient.get().uri("/needyoken")
            .header(TOKEN_HEADER_NAME, authServer.toToken(builder))
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody(ProblemDetail.class).isEqualTo(detail);
    }
}
