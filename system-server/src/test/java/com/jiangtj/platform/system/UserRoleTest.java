package com.jiangtj.platform.system;

import com.jiangtj.platform.system.entity.SystemUserRole;
import com.jiangtj.platform.test.cloud.JMicroCloudFluxTest;
import com.jiangtj.platform.test.cloud.UserToken;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Slf4j
@JMicroCloudFluxTest
class UserRoleTest {

    @Resource
    WebTestClient client;
    @Resource
    R2dbcEntityTemplate template;

    @Test
    @UserToken
    @DisplayName("get system user role relation")
    void getRoleList() {
        List<String> roles = template.select(SystemUserRole.class)
            .matching(query(where("user_id").is(1)))
            .all()
            .map(SystemUserRole::getRole)
            .collectList()
            .block();

        assert roles != null;

        client.get().uri(UriComponentsBuilder
                .fromUriString("/user/{id}/roles")
                .build(1))
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<List<String>>() {})
            .isEqualTo(roles);
    }

    @Test
    @UserToken
    @DisplayName("change system user role relation")
    void changeUserRole() {
        client.put().uri(UriComponentsBuilder
                .fromUriString("/user/{id}/roles")
                .build(1))
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(List.of("system", "test_r o-le"))
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<List<String>>() {})
            .value(l -> Assertions.assertTrue(l.contains("system")))
            .value(l -> Assertions.assertTrue(l.contains("testrole")));
    }

}