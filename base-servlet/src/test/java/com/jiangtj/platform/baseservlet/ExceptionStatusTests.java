package com.jiangtj.platform.baseservlet;

import com.jiangtj.platform.test.cloud.JMicroCloudMvcTest;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;

@JMicroCloudMvcTest
public class ExceptionStatusTests {
    @Resource
    WebTestClient webClient;

    @Test
    void testErr2() {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        detail.setTitle(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        detail.setDetail("系统错误");
        detail.setInstance(URI.create("/insecure/err2"));
        webClient.get().uri("/insecure/err2")
            .exchange()
            .expectStatus().is5xxServerError()
            .expectBody(ProblemDetail.class).isEqualTo(detail);
    }

    @Test
    void testFnErr2() {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        detail.setTitle(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        detail.setDetail("系统错误");
        detail.setInstance(URI.create("/insecure/fn/err2"));
        webClient.get().uri("/insecure/fn/err2")
            .exchange()
            .expectStatus().is5xxServerError()
            .expectBody(ProblemDetail.class).isEqualTo(detail);
    }
}
