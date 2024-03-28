package com.jiangtj.platform.system;

import com.jiangtj.platform.auth.servlet.AuthHolder;
import com.jiangtj.platform.sql.jooq.PageUtils;
import com.jiangtj.platform.system.dto.LoginDto;
import com.jiangtj.platform.system.dto.LoginResultDto;
import com.jiangtj.platform.system.dto.PasswordUpdateDto;
import com.jiangtj.platform.system.entity.SystemUser;
import com.jiangtj.platform.system.jooq.tables.records.SystemUserRecord;
import com.jiangtj.platform.web.BaseExceptionUtils;
import jakarta.annotation.Resource;
import org.jooq.DSLContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static com.jiangtj.platform.system.jooq.Tables.SYSTEM_USER;
import static org.jooq.impl.DSL.noCondition;

@Service
public class UserService {

    @Resource
    private DSLContext create;
    @Resource
    private UserRoleService userRoleService;

    public LoginResultDto login(LoginDto dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
            throw BaseExceptionUtils.badRequest("请输入用户名与密码！");
        }

        SystemUser user = create.selectFrom(SYSTEM_USER)
            .where(SYSTEM_USER.USERNAME.eq(username))
            .and(SYSTEM_USER.IS_DELETED.eq((byte) 0))
            .fetchOneInto(SystemUser.class);
        if (user == null) {
            throw BaseExceptionUtils.badRequest("用户不存在");
        }
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
            throw BaseExceptionUtils.badRequest("密码错误！");
        }
        Long id = user.getId();
        List<String> roles = userRoleService.getUserRoles(id);
        return LoginResultDto.of(user, roles);
    }

    public SystemUser getAdminUser(Long id) {
        return create.selectFrom(SYSTEM_USER)
            .where(SYSTEM_USER.ID.eq(id))
            .fetchOneInto(SystemUser.class);
    }

    public boolean isExistsName(SystemUser user) {
        return create.selectFrom(SYSTEM_USER)
            .where(SYSTEM_USER.USERNAME.eq(user.getUsername()))
            .and(SYSTEM_USER.IS_DELETED.eq((byte) 0))
            .fetch()
            .isNotEmpty();
        /*return template.select(SystemUser.class)
            .matching(query(where("username").is(user.getUsername()).and(DbUtils.notDel())))
            .exists()
            .doOnNext(isE -> {
                if (isE)
            })
            .thenReturn(user);*/
    }

    public SystemUser createAdminUser(SystemUser user) {
        if (isExistsName(user)) {
            throw BaseExceptionUtils.badRequest("不能创建一样的名字！");
        }
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        SystemUserRecord record = create.newRecord(SYSTEM_USER, user);
        record.store();
        return record.into(SystemUser.class);
    }

    public SystemUser updateAdminUser(SystemUser user) {
        Long id = user.getId();
        String username = user.getUsername();
        Objects.requireNonNull(id);
        Objects.requireNonNull(username);
        if (isExistsName(user)) {
            throw BaseExceptionUtils.badRequest("不能创建一样的名字！");
        }

        SystemUserRecord record = create.newRecord(SYSTEM_USER, user);
        record.changed(SYSTEM_USER.ID, false);
        record.store();
        return record.into(SystemUser.class);
    }

    public void deleteAdminUser(Long id) {
        create.deleteFrom(SYSTEM_USER)
            .where(SYSTEM_USER.ID.eq(id))
            .execute();
    }

    public Page<SystemUser> getAdminUserPage(SystemUser user, Pageable pageable) {
        return PageUtils.selectFrom(create, SYSTEM_USER)
            .conditions(SYSTEM_USER.IS_DELETED.eq((byte) 0)
                .and(StringUtils.hasLength(user.getUsername()) ?
                    SYSTEM_USER.USERNAME.like(user.getUsername() + "%") :
                    noCondition()))
            .pageable(pageable)
            .fetchPage(SystemUser.class);
    }

    public void updateAdminPassword(PasswordUpdateDto body) {
        Long adminId = getRequiredCurrentUserId();
        SystemUserRecord fetched = create.selectFrom(SYSTEM_USER)
            .where(SYSTEM_USER.ID.eq(adminId))
            .fetchOptional()
            .orElseThrow();
        String old = DigestUtils.md5DigestAsHex(body.getOld().getBytes());
        if (!old.equals(fetched.getPassword())) {
            throw BaseExceptionUtils.badRequest("请确认你的密码");
        }
        fetched.setPassword(DigestUtils.md5DigestAsHex(body.getPassword().getBytes()));
        fetched.store();
    }

    public Long getRequiredCurrentUserId() {
        return Long.parseLong(AuthHolder.getAuthContext().subject());
    }
}
