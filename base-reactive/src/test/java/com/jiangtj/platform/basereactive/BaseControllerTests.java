package com.jiangtj.platform.basereactive;

import com.jiangtj.platform.test.ProblemDetailConsumer;
import com.jiangtj.platform.test.cloud.JMicroCloudTest;
import com.jiangtj.platform.test.cloud.UserToken;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;

@JMicroCloudTest
@AutoConfigureWebTestClient
class BaseControllerTests {
    
    @Resource
    WebTestClient client;

    @Test
    void testIndex() {
        client.get().uri("/")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class).isEqualTo("Base Reactive Client Started !!");
    }

    @Test
    void testErr() {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
        detail.setDetail("insecure");
        detail.setInstance(URI.create("/insecure/err"));
        client.get().uri("/insecure/err")
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody(ProblemDetail.class).isEqualTo(detail);
    }

    @Test
    @UserToken
    void testHaveToken() {
        client.get().uri("/needtoken")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void testNotHaveToken() {
        client.get().uri("/needtoken")
            .exchange()
            .expectAll(ProblemDetailConsumer.unLogin().expect());
    }

    @Test
    @UserToken(role = "role-test1")
    void testHaveRoleAnnotations() {
        client.get().uri("/role-test-1")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    @UserToken(role = "role-test1")
    void testNotHaveRoleAnnotations() {
        client.get().uri("/role-test-2")
            .exchange()
            .expectAll(ProblemDetailConsumer.unRole("roletest2").expect());
    }

}
