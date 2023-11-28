package com.jiangtj.platform.basereactive;

import com.jiangtj.platform.test.JCloudWebClientBuilder;
import com.jiangtj.platform.test.JCloudWebTest;
import com.jiangtj.platform.test.ProblemDetailConsumer;
import com.jiangtj.platform.test.UserToken;
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

    @Test
    @UserToken(role = "role-test1")
    void testHaveRoleAnnotations(JCloudWebClientBuilder client) {
        client.build().get().uri("/role-test-1")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    @UserToken(role = "role-test1")
    void testNotHaveRoleAnnotations(JCloudWebClientBuilder client) {
        client.build().get().uri("/role-test-2")
            .exchange()
            .expectAll(ProblemDetailConsumer.unRole("roletest2").expect());
    }

}
