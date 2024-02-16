package com.jiangtj.platform.baseservlet;

import com.jiangtj.platform.auth.cloud.AuthServer;
import com.jiangtj.platform.test.cloud.JMicroCloudMvcTest;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;

import static com.jiangtj.platform.auth.AuthRequestAttributes.TOKEN_HEADER_NAME;

@JMicroCloudMvcTest
class BaseControllerTests {

    @Resource
    WebTestClient webClient;
    @Resource
    AuthServer authServer;

    @Test
    void testIndex() {
        webClient.get().uri("/")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class).isEqualTo("Base Servlet Client Started !!");
    }

    @Test
    void testErr() {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
        detail.setDetail("insecure");
        detail.setInstance(URI.create("/insecure/err"));
        webClient.get().uri("/insecure/err")
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody(ProblemDetail.class).isEqualTo(detail);
    }

    @Test
    void testHaveToken() {
        String token = authServer.createServerToken(authServer.getApplicationName());
        webClient.get().uri("/needtoken")
            .header(TOKEN_HEADER_NAME, token)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void testNotHaveToken() {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        detail.setTitle(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        detail.setDetail("缺少认证信息，请在header中携带token");
        detail.setInstance(URI.create("/needtoken"));
        webClient.get().uri("/needtoken")
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody(ProblemDetail.class).isEqualTo(detail);
    }

}