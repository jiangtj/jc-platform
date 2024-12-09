package com.jiangtj.platform.system;

import com.jiangtj.platform.test.ProblemDetailConsumer;
import com.jiangtj.platform.test.cloud.JMicroCloudMvcTest;
import com.jiangtj.platform.test.cloud.UserToken;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;

@JMicroCloudMvcTest
class PermissionTest {

    @Resource
    WebTestClient client;

    @Test
    @DisplayName("test no user token")
    void testNoU() {
        client.get().uri("/roles/name")
            .exchange()
            .expectAll(ProblemDetailConsumer.unLogin().expect());
    }

    @Test
    @UserToken(id = 2, role = "system-readonly")
    @DisplayName("test no permission")
    void testNoP() {
        client.post().uri("/user")
            .exchange()
            .expectAll(ProblemDetailConsumer
                .unPermission("system:user:write")
                .expect());
    }

    @Test
    @UserToken(id = 2, role = "system-readonly")
    @DisplayName("system-read can query user page")
    void testSystemR() {
        client.get().uri(UriComponentsBuilder
                .fromUriString("/user/page")
                .queryParam("username", "ad")
                .build().toUri())
            .exchange()
            .expectStatus().isOk();
    }

}
