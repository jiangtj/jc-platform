package com.jiangtj.cloud.system;

import com.jiangtj.cloud.test.JCloudWebClientBuilder;
import com.jiangtj.cloud.test.JCloudWebTest;
import com.jiangtj.cloud.test.UserToken;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

@Slf4j
@JCloudWebTest
class RoleTest {

    @Resource
    private RoleService roleService;

    List<RoleService.ServerRole> source = List.of(
        new RoleService.ServerRole("server1", List.of("p 1","p-2","p3")),
        new RoleService.ServerRole("server2", Collections.emptyList()),
        new RoleService.ServerRole("server3", List.of("p4","p-3"))
    );

    List<RoleService.ServerRole> keys = List.of(
        new RoleService.ServerRole("server1", List.of("p1","p2","p3")),
        new RoleService.ServerRole("server2", Collections.emptyList()),
        new RoleService.ServerRole("server3", List.of("p4","p3"))
    );

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(roleService, "serverRoles", source);
    }

    @Test
    @UserToken
    @DisplayName("get roles name")
    void getRole(JCloudWebClientBuilder client) {
        client.build().get().uri("/roles/name")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(RoleService.ServerRole.class)
            .isEqualTo(source);
    }

    @Test
    @UserToken
    @DisplayName("get roles key")
    void getRoleKey(JCloudWebClientBuilder client) {
        client.build().get().uri("/roles/key")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(RoleService.ServerRole.class)
            .isEqualTo(keys);
    }

    @Test
    @Disabled
    @UserToken(role = {"actuator"})
    @DisplayName("get role info")
    void getRoleInfo(JCloudWebClientBuilder client) {
        // 思考怎么实现。。。
    }

}
