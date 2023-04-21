package com.jtj.cloud.basereactive;

import com.jtj.cloud.test.JCloudWebClientBuilder;
import com.jtj.cloud.test.JCloudWebTest;
import com.jtj.cloud.test.ProblemDetailConsumer;
import com.jtj.cloud.test.UserToken;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;

@JCloudWebTest
class BaseControllerTests {

    @Test
    void testIndex(JCloudWebClientBuilder client) {
        client.build().get().uri("/")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class).isEqualTo("Base Reactive Client Started !!");
    }

    @Test
    void testErr(JCloudWebClientBuilder client) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
        detail.setDetail("insecure");
        detail.setInstance(URI.create("/insecure/err"));
        client.build().get().uri("/insecure/err")
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody(ProblemDetail.class).isEqualTo(detail);
    }

    @Test
    @UserToken
    void testHaveToken(JCloudWebClientBuilder client) {
        client.build().get().uri("/needtoken")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void testNotHaveToken(JCloudWebClientBuilder client) {
        client.build().get().uri("/needtoken")
            .exchange()
            .expectAll(ProblemDetailConsumer.unLogin().expect());
    }

}
