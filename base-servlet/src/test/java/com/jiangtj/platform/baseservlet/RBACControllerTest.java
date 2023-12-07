package com.jiangtj.platform.baseservlet;

import com.jiangtj.platform.test.ProblemDetailConsumer;
import com.jiangtj.platform.test.cloud.JMicroCloudMvcTest;
import com.jiangtj.platform.test.cloud.UserToken;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

@JMicroCloudMvcTest
class RBACControllerTest {

    @Resource
    WebTestClient client;

    @Test
    @UserToken
    void hasLoginWithToken() {
        client.get().uri("/anno/hasLogin")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void hasLoginWithoutToken() {
        client.get().uri("/anno/hasLogin")
                .exchange()
                .expectAll(ProblemDetailConsumer.unLogin().expect());
    }
}