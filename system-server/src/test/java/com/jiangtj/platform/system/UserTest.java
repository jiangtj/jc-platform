package com.jiangtj.platform.system;

import com.jiangtj.platform.system.dto.LoginDto;
import com.jiangtj.platform.system.dto.LoginResultDto;
import com.jiangtj.platform.system.dto.PasswordUpdateDto;
import com.jiangtj.platform.system.entity.SystemUser;
import com.jiangtj.platform.test.JCloudWebClientBuilder;
import com.jiangtj.platform.test.JCloudWebTest;
import com.jiangtj.platform.test.ProblemDetailConsumer;
import com.jiangtj.platform.test.UserToken;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@JCloudWebTest
class UserTest {

    @Resource
    R2dbcEntityTemplate template;

    @Test
    @DisplayName("login system user")
    void login(JCloudWebClientBuilder client) {
        LoginDto dto = new LoginDto();
        dto.setUsername("admin");
        dto.setPassword("123456");
        client.build().post().uri("/login")
            .bodyValue(dto)
            .exchange()
            .expectStatus().isOk()
            .expectBody(LoginResultDto.class)
            .value(result -> {
                assertEquals(1, result.getUser().getId());
            });

        dto.setPassword("1234567");
        client.build().post().uri("/login")
            .bodyValue(dto)
            .exchange()
            .expectAll(ProblemDetailConsumer.forStatus(HttpStatus.BAD_REQUEST)
                .detail("密码错误！")
                .expect());

        dto.setUsername("no-user");
        client.build().post().uri("/login")
            .bodyValue(dto)
            .exchange()
            .expectAll(ProblemDetailConsumer.forStatus(HttpStatus.BAD_REQUEST)
                .detail("用户不存在")
                .expect());
    }

    @Test
    @UserToken
    @DisplayName("get system user page")
    void getPage(JCloudWebClientBuilder client) {
        client.build().get().uri(UriComponentsBuilder
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
    void changePassword(JCloudWebClientBuilder client) {
        PasswordUpdateDto body = new PasswordUpdateDto();
        body.setOld("123456");
        body.setPassword("1234567");
        client.build().post().uri("/user/password")
            .bodyValue(body)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .isEmpty();
    }

    @Test
    @UserToken
    @DisplayName("create a new user")
    void postNewUser(JCloudWebClientBuilder client) {
        client.build().post().uri("/user")
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
    void changeUser(JCloudWebClientBuilder client) {
        SystemUser user3 = createUserEntity("user3");
        user3 = template.insert(user3).block();
        Objects.requireNonNull(user3.getId());
        user3.setUsername("changeusername");
        client.build().put().uri("/user")
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
    void deleteUser(JCloudWebClientBuilder client) {
        SystemUser user4 = createUserEntity("user4");
        user4 = template.insert(user4).block();
        Objects.requireNonNull(user4.getId());
        client.build().delete().uri("/user/" + user4.getId())
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