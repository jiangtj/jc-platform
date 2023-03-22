package com.jtj.cloud.baseservlet;

import com.jtj.cloud.auth.AuthServer;
import com.jtj.cloud.auth.TokenType;
import com.jtj.cloud.baseservlet.base.AbstractServerTests;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;
import java.time.Duration;

public class JwtExceptionTests extends AbstractServerTests {
    @Resource
    WebTestClient webClient;
    @Resource
    AuthServer authServer;

    @Test
    void testJwtException() {
        String token = authServer.builder()
            .setAuthType(TokenType.SYSTEM)
            .setExpires(Duration.ofMinutes(50))
            .build();
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        detail.setTitle("Invalid Token");
        detail.setDetail("ExpiresTime is bigger than max expires time!");
        detail.setInstance(URI.create("/needyoken"));
        webClient.get().uri("/needyoken")
            .header(authServer.getProperties().getHeaderName(), token)
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody(ProblemDetail.class).isEqualTo(detail);
    }
}
