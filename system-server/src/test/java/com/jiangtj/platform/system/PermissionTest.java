package com.jiangtj.platform.system;

import com.jiangtj.platform.test.JCloudWebClientBuilder;
import com.jiangtj.platform.test.JCloudWebTest;
import com.jiangtj.platform.test.ProblemDetailConsumer;
import com.jiangtj.platform.test.UserToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

@JCloudWebTest
class PermissionTest {

    @Test
    @DisplayName("test no user token")
    void testNoU(JCloudWebClientBuilder client) {
        client.build().get().uri("/")
            .exchange()
            .expectAll(ProblemDetailConsumer.unLogin().expect());
    }

    @Test
    @UserToken(id = 2, role = "system-readonly")
    @DisplayName("test no permission")
    void testNoP(JCloudWebClientBuilder client) {
        client.build().post().uri("/user")
            .exchange()
            .expectAll(ProblemDetailConsumer
                .unPermission("system:user:write")
                .expect());
    }

    @Test
    @UserToken(id = 2, role = "system-readonly")
    @DisplayName("system-read can query user page")
    void testSystemR(JCloudWebClientBuilder client) {
        client.build().get().uri(UriComponentsBuilder
                .fromUriString("/user/page")
                .queryParam("username", "ad")
                .build().toUri())
            .exchange()
            .expectStatus().isOk();
    }

}
