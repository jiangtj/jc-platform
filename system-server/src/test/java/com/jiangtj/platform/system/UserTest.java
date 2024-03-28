package com.jiangtj.platform.system;

import com.jiangtj.platform.system.dto.LoginDto;
import com.jiangtj.platform.system.dto.LoginResultDto;
import com.jiangtj.platform.system.dto.PasswordUpdateDto;
import com.jiangtj.platform.system.entity.SystemUser;
import com.jiangtj.platform.system.jooq.tables.records.SystemUserRecord;
import com.jiangtj.platform.test.ProblemDetailConsumer;
import com.jiangtj.platform.test.cloud.JMicroCloudMvcTest;
import com.jiangtj.platform.test.cloud.UserToken;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

import static com.jiangtj.platform.system.jooq.Tables.SYSTEM_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@JMicroCloudMvcTest
class UserTest {

    @Resource
    WebTestClient client;
    @Resource
    DSLContext create;

    @Test
    @DisplayName("login system user")
    void login() {
        LoginDto dto = new LoginDto();
        dto.setUsername("admin");
        dto.setPassword("123456");
        client.post().uri("/login")
            .bodyValue(dto)
            .exchange()
            .expectStatus().isOk()
            .expectBody(LoginResultDto.class)
            .value(result -> {
                assertEquals(1, result.getUser().getId());
            });

        dto.setPassword("1234567");
        client.post().uri("/login")
            .bodyValue(dto)
            .exchange()
            .expectAll(ProblemDetailConsumer.forStatus(HttpStatus.BAD_REQUEST)
                .detail("密码错误！")
                .expect());

        dto.setUsername("no-user");
        client.post().uri("/login")
            .bodyValue(dto)
            .exchange()
            .expectAll(ProblemDetailConsumer.forStatus(HttpStatus.BAD_REQUEST)
                .detail("用户不存在")
                .expect());
    }

    @Test
    @UserToken
    @DisplayName("get system user page")
    void getPage() {
        client.get().uri(UriComponentsBuilder
                .fromUriString("/user/page")
                .queryParam("username", "ad")
                .build().toUri())
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("totalElements").isEqualTo(1)
            .jsonPath("content[0].id").isEqualTo(1)
            .jsonPath("content[0].password").doesNotExist();
    }

    @Test
    @UserToken
    @DisplayName("change system user password")
    void changePassword() {
        PasswordUpdateDto body = new PasswordUpdateDto();
        body.setOld("123456");
        body.setPassword("1234567");
        client.post().uri("/user/password")
            .bodyValue(body)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .isEmpty();
    }

    @Test
    @UserToken
    @DisplayName("create a new user")
    void postNewUser() {
        client.post().uri("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("{\"username\":\"testuser2\", \"password\":\"testpassword\"}")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("username").isEqualTo("testuser2")
            .jsonPath("password").doesNotExist();
    }

    @Test
    @UserToken
    @DisplayName("change user name")
    void changeUser() {
        SystemUser user3 = createUserEntity("user3");
        SystemUserRecord record = create.newRecord(SYSTEM_USER, user3);
        record.store();
        user3 = record.into(SystemUser.class);
        Objects.requireNonNull(user3.getId());
        user3.setUsername("changeusername");
        client.put().uri("/user")
            .bodyValue(user3)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("id").isEqualTo(user3.getId())
            .jsonPath("username").isEqualTo("changeusername")
            .jsonPath("password").doesNotExist();
    }

    @Test
    @UserToken
    @DisplayName("delete user by id")
    void deleteUser() {
        SystemUser user4 = createUserEntity("user4");
        SystemUserRecord record = create.newRecord(SYSTEM_USER, user4);
        record.store();
        user4 = record.into(SystemUser.class);
        Objects.requireNonNull(user4.getId());
        client.delete().uri("/user/" + user4.getId())
            .exchange()
            .expectStatus().isNoContent();
    }

    SystemUser createUserEntity(String name) {
        SystemUser user = new SystemUser();
        user.setUsername(name);
        user.setPassword("testpassword");
        return user;
    }
}