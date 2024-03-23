package com.jiangtj.platform.baseservlet;

import com.jiangtj.platform.test.ProblemDetailConsumer;
import com.jiangtj.platform.test.cloud.JMicroCloudMvcTest;
import com.jiangtj.platform.test.cloud.UserToken;
import com.jiangtj.platform.test.cloud.WithServer;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

@JMicroCloudMvcTest
public class ServerTokenTests {

    @Resource
    WebTestClient client;

    @Test
    void testGetActuatorWithoutToken() {
        client.get().uri("/actuator")
                .exchange()
                .expectAll(ProblemDetailConsumer.unLogin().expect());
    }

    @Test
    @UserToken
    void testGetActuatorWithUserToken() {
        client.get().uri("/actuator")
                .exchange()
                .expectAll(ProblemDetailConsumer.unInvalid("不支持的 Auth Context").expect());
    }

    @Test
    @WithServer("any")
    void testGetActuatorWithServerToken() {
        client.get().uri("/actuator")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithServer("test-issuer")
    void testGetAnnotationWithServerToken() {
        client.get().uri("/call-with-server")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithServer("test-issuer-fail")
    void testGetAnnotationWithFailServerToken() {
        client.get().uri("/call-with-server")
                .exchange()
                .expectAll(ProblemDetailConsumer.unInvalid("不支持的 Auth Context").expect());
    }

    @Test
    void testGetAnnotationWithoutToken() {
        client.get().uri("/call-with-server")
                .exchange()
                .expectAll(ProblemDetailConsumer.unLogin().expect());
    }
}
