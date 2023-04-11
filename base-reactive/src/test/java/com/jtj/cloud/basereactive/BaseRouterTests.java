package com.jtj.cloud.basereactive;

import com.jtj.cloud.auth.AuthServer;
import com.jtj.cloud.auth.TokenType;
import com.jtj.cloud.basereactive.base.AbstractServerTests;
import com.jtj.cloud.test.ProblemDetails;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;

import static com.jtj.cloud.auth.RequestAttributes.TOKEN_HEADER_NAME;

class BaseRouterTests extends AbstractServerTests {

    @Resource
    WebTestClient webClient;
    @Resource
    AuthServer authServer;

    @Test
    void testErr() {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
        detail.setDetail("fn");
        detail.setInstance(URI.create("/insecure/fn/err"));
        webClient.get().uri("/insecure/fn/err")
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody(ProblemDetail.class).isEqualTo(detail);
    }

    @Test
    void testHaveToken() {
        String token = authServer.builder()
            .setAuthType(TokenType.SERVER)
            .setAudience(authServer.getApplicationName())
            .build();
        webClient.get().uri("/fn/needtoken")
            .header(TOKEN_HEADER_NAME, token)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void testNotHaveToken() {
        webClient.get().uri("/fn/needtoken")
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody(ProblemDetail.class)
            .isEqualTo(ProblemDetails.unLogin("/fn/needtoken"));
    }

}
