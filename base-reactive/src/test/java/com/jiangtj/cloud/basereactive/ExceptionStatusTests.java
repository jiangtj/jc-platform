package com.jiangtj.cloud.basereactive;

import com.jiangtj.cloud.test.JCloudWebClientBuilder;
import com.jiangtj.cloud.test.JCloudWebTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;

@JCloudWebTest
class ExceptionStatusTests {

    @Test
    void testErr2(JCloudWebClientBuilder client) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        detail.setTitle(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        detail.setDetail("系统错误");
        detail.setInstance(URI.create("/insecure/err2"));
        client.build().get().uri("/insecure/err2")
            .exchange()
            .expectStatus().is5xxServerError()
            .expectBody(ProblemDetail.class).isEqualTo(detail);
    }

    @Test
    void testFnErr2(JCloudWebClientBuilder client) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        detail.setTitle(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        detail.setDetail("系统错误");
        detail.setInstance(URI.create("/insecure/fn/err2"));
        client.build().get().uri("/insecure/fn/err2")
            .exchange()
            .expectStatus().is5xxServerError()
            .expectBody(ProblemDetail.class).isEqualTo(detail);
    }
}
