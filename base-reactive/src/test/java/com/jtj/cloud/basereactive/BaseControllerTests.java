package com.jtj.cloud.basereactive;

import com.jtj.cloud.auth.AuthServer;
import com.jtj.cloud.auth.TokenType;
import com.jtj.cloud.basereactive.base.AbstractServerTests;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;

class BaseControllerTests extends AbstractServerTests {

    @Resource
    WebTestClient webClient;
    @Resource
    AuthServer authServer;

    @Test
    void testIndex() {
        webClient.get().uri("/")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class).isEqualTo("Base Reactive Client Started !!");
    }

    @Test
    void testErr() {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        detail.setTitle("无效的Token");
        detail.setDetail("insecure");
        webClient.get().uri("/insecure/err")
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
        webClient.get().uri("/needtoken")
            .header(authServer.getProperties().getHeaderName(), token)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void testNotHaveToken() {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        detail.setTitle("无效的Token");
        detail.setDetail("缺少有效的 token！");
        webClient.get().uri("/needtoken")
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody(ProblemDetail.class).isEqualTo(detail);
    }

}
