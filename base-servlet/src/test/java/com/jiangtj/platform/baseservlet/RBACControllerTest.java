package com.jiangtj.platform.baseservlet;

import com.jiangtj.platform.test.JMicroMvcTest;
import com.jiangtj.platform.test.JMicroTest;
import com.jiangtj.platform.test.ProblemDetailConsumer;
import com.jiangtj.platform.test.WithMockUser;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

@JMicroMvcTest
class RBACControllerTest {

    @Resource
    WebTestClient client;

    @Test
    @WithMockUser
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