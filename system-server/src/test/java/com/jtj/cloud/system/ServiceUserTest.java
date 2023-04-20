package com.jtj.cloud.system;

import com.jtj.cloud.system.dto.LoginDto;
import com.jtj.cloud.system.dto.LoginResultDto;
import com.jtj.cloud.system.dto.PasswordUpdateDto;
import com.jtj.cloud.test.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@JCloudWebTest
@EnableTransactionManagement
class ServiceUserTest {

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
            .jsonPath("content[0].id").isEqualTo(1);
    }

    @Test
    @UserToken
    @Rollback
    @DisplayName("change system user password")
    void changePassword(JCloudWebClientBuilder client) {
        PasswordUpdateDto body = new PasswordUpdateDto();
        body.setOld("1234567");
        body.setPassword("123456");
        client.build().post().uri("/user/password")
            .bodyValue(body)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .isEmpty();
    }

    /*@Test

    @Test
    void getAdminUser() {
        adminService.getAdminUser(2L)
            .as(StepVerifier::create)
            .assertNext(user -> {
                assertEquals("testuser", user.getUsername());
            })
            .verifyComplete();
    }

    AdminUser createUserDto() {
        AdminUser user = new AdminUser();
        user.setUsername("testuser2");
        user.setPassword("123456");
        return user;
    }

    @Test
    void createAdminUser() {
        AdminUser user = createUserDto();
        adminService.createAdminUser(user)
            .as(TxStepVerifier::withRollback)
            .assertNext(adminUser -> {
                assertEquals(user.getUsername(), adminUser.getUsername());
            })
            .verifyComplete();

        user.setUsername("testuser");
        adminService.createAdminUser(user)
            .as(TxStepVerifier::withRollback)
            .verifyErrorMessage("不能创建一样的名字！");
    }

    @Test
    void updateAdminUser() {
        AdminUser user = new AdminUser();
        user.setId(2L);
        user.setUsername("cjkane");
        adminService.updateAdminUser(user)
            .as(TxStepVerifier::withRollback)
            .expectNext(1L)
            .verifyComplete();

        user.setUsername("exuser");
        adminService.updateAdminUser(user)
            .as(TxStepVerifier::withRollback)
            .verifyErrorMessage("exuser名字已存在！");
    }

    @Test
    void deleteAdminUser() {
        adminService.deleteAdminUser(2L)
            .as(TxStepVerifier::withRollback)
            .expectNext(1L)
            .verifyComplete();
        adminService.deleteAdminUser(-123L)
            .as(TxStepVerifier::withRollback)
            .expectNext(0L)
            .verifyComplete();
    }

    @Test
    void getAdminUserPage() {
        Long all = super.template.select(AdminUser.class).matching(query(DbUtils.notDel())).count().block();
        adminService.getAdminUserPage(new AdminUser(), Pageable.ofSize(2))
            .as(StepVerifier::create)
            .assertNext(page -> {
                long totalElements = page.getTotalElements();
                assertEquals(all, totalElements);
                assertEquals(2, page.getContent().size());
            })
            .verifyComplete();
    }

    @Test
    void updateAdminPassword() {
        PasswordUpdateDto dto = new PasswordUpdateDto();
        dto.setAdminId(2L);
        dto.setOld("123456");
        dto.setPassword("654");
        adminService.updateAdminPassword(dto)
            .as(TxStepVerifier::withRollback)
            .expectNext(1L)
            .verifyComplete();

        dto.setOld("qwvw34");
        adminService.updateAdminPassword(dto)
            .as(TxStepVerifier::withRollback)
            .verifyErrorMessage("请确认你的密码");
    }*/

    @Test
    void getRequiredCurrentUserId() {
    }

    @Test
    void getCurrentUserId() {
    }

    @Test
    void getCurrentClaims() {
    }
}