package com.jiangtj.cloud.baseservlet;

import com.jiangtj.cloud.test.JCloudWebClientBuilder;
import com.jiangtj.cloud.test.JCloudWebMvcTest;
import com.jiangtj.cloud.test.ProblemDetailConsumer;
import com.jiangtj.cloud.test.UserToken;
import org.junit.jupiter.api.Test;

@JCloudWebMvcTest
class RBACControllerTest {

    @Test
    @UserToken
    void hasLoginWithToken(JCloudWebClientBuilder client) {
        client.build().get().uri("/anno/hasLogin")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void hasLoginWithoutToken(JCloudWebClientBuilder client) {
        client.build().get().uri("/anno/hasLogin")
                .exchange()
                .expectAll(ProblemDetailConsumer.unLogin().expect());
    }
}