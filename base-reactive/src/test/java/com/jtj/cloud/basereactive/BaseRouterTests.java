package com.jtj.cloud.basereactive;

import com.jtj.cloud.test.JCloudWebClientBuilder;
import com.jtj.cloud.test.JCloudWebTest;
import com.jtj.cloud.test.ProblemDetails;
import com.jtj.cloud.test.UserToken;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;

@JCloudWebTest
class BaseRouterTests {

    @Test
    void testErr(JCloudWebClientBuilder client) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
        detail.setDetail("fn");
        detail.setInstance(URI.create("/insecure/fn/err"));
        client.build().get().uri("/insecure/fn/err")
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody(ProblemDetail.class).isEqualTo(detail);
    }

    @Test
    @UserToken
    void testHaveToken(JCloudWebClientBuilder client) {
        client.build().get().uri("/fn/needtoken")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void testNotHaveToken(JCloudWebClientBuilder client) {
        client.build().get().uri("/fn/needtoken")
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody(ProblemDetail.class)
            .isEqualTo(ProblemDetails.unLogin("/fn/needtoken"));
    }

}
