package com.jiangtj.platform.baseservlet;

import com.jiangtj.platform.test.JCloudWebClientBuilder;
import com.jiangtj.platform.test.JCloudWebMvcTest;
import com.jiangtj.platform.test.ProblemDetailConsumer;
import com.jiangtj.platform.test.UserToken;
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