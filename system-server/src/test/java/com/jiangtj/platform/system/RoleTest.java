package com.jiangtj.platform.system;

import com.jiangtj.platform.test.cloud.JMicroCloudMvcTest;
import com.jiangtj.platform.test.cloud.UserToken;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Collections;
import java.util.List;

@Slf4j
@JMicroCloudMvcTest
class RoleTest {

    @Resource
    WebTestClient client;

    @Resource
    private RoleService roleService;

    List<ServerRole> source = List.of(
        new ServerRole("server1", List.of("p 1","p-2","p3")),
        new ServerRole("server2", Collections.emptyList()),
        new ServerRole("server3", List.of("p4","p-3"))
    );

    List<ServerRole> keys = List.of(
        new ServerRole("server1", List.of("p1","p2","p3")),
        new ServerRole("server2", Collections.emptyList()),
        new ServerRole("server3", List.of("p4","p3"))
    );

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(roleService, "serverRoles", source);
    }

    @Test
    @UserToken
    @DisplayName("get roles name")
    void getRole() {
        client.get().uri("/roles/name")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ServerRole.class)
            .isEqualTo(source);
    }

    @Test
    @UserToken
    @DisplayName("get roles key")
    void getRoleKey() {
        client.get().uri("/roles/key")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ServerRole.class)
            .isEqualTo(keys);
    }

    @Test
    @Disabled
    @UserToken(role = {"actuator"})
    @DisplayName("get role info")
    void getRoleInfo() {
        // 思考怎么实现。。。
    }

}
