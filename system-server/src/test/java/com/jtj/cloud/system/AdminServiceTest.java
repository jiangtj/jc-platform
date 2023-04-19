package com.jtj.cloud.system;

import com.jtj.cloud.test.JCloudWebTest;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
@JCloudWebTest
class AdminServiceTest {

    @Resource
    SystemUserService userService;

    /*@Test
    void login() {
        SystemUser user = new SystemUser();
        user.setUsername("testuser");
        user.setPassword("123456");
        userService.login(user)
            .as(TxStepVerifier::withRollback)
            .assertNext(loginResultDto -> {
                assertNotNull(loginResultDto.getToken());
                assertNotNull(loginResultDto.getUser());
                assertEquals("testuser", loginResultDto.getUser().getUsername());
            })
            .verifyComplete();

        user.setPassword("wef32");
        adminService.login(user)
            .as(TxStepVerifier::withRollback)
            .expectError(BadRequestException.class)
            .verify();
    }

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