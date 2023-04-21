package com.jtj.cloud.system;

import com.jtj.cloud.auth.AuthServer;
import com.jtj.cloud.auth.UserClaims;
import com.jtj.cloud.auth.reactive.AuthReactorHolder;
import com.jtj.cloud.common.BaseExceptionUtils;
import com.jtj.cloud.sql.reactive.DbUtils;
import com.jtj.cloud.system.dto.LoginDto;
import com.jtj.cloud.system.dto.LoginResultDto;
import com.jtj.cloud.system.dto.PasswordUpdateDto;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;
import static org.springframework.data.relational.core.query.Update.update;

@Service
public class SystemUserService {

    @Resource
    private AuthServer authServer;

    @Resource
    private R2dbcEntityTemplate template;

    public Mono<LoginResultDto> login(LoginDto dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
            throw BaseExceptionUtils.badRequest("请输入用户名与密码！");
        }
        return template.select(SystemUser.class)
            .matching(query(where("username").is(username).and(DbUtils.notDel())))
            .one()
            .switchIfEmpty(Mono.error(BaseExceptionUtils.badRequest("用户不存在")))
            .filter(item -> DigestUtils.md5DigestAsHex(password.getBytes()).equals(item.getPassword()))
            .switchIfEmpty(Mono.error(BaseExceptionUtils.badRequest("密码错误！")))
            .map(item -> {
                Long id = item.getId();
                List<String> roles = new ArrayList<>();
                // todo 获取角色
                return LoginResultDto.of(item, UserClaims.builder()
                        .id(String.valueOf(id))
                        .roles(roles)
                    .build());
            });
    }

    public Mono<SystemUser> getAdminUser(Long id) {
        return DbUtils.findById(template, id, SystemUser.class);
    }

    public Mono<Boolean> isExistsName(String username) {
        return template.select(SystemUser.class)
            .matching(query(where("username").is(username).and(DbUtils.notDel())))
            .exists();
    }

    public Mono<SystemUser> createAdminUser(SystemUser user) {
        Mono<SystemUser> insert = Mono.just(user)
            .doOnNext(systemUser -> systemUser.setPassword(DigestUtils.md5DigestAsHex(systemUser.getPassword().getBytes())))
            .flatMap(systemUser -> DbUtils.insert(template, systemUser));
        return isExistsName(user.getUsername())
            .doOnNext(isE -> {
                if (isE) throw BaseExceptionUtils.badRequest("不能创建一样的名字！");
            })
            .then(insert);
    }

    public Mono<Long> updateAdminUser(SystemUser user) {
        Long id = user.getId();
        String username = user.getUsername();
        Objects.requireNonNull(id);
        Objects.requireNonNull(username);
        return template.update(SystemUser.class)
            .matching(query(where("id").is(id)))
            .apply(update("username", username))
            .as(update -> isExistsName(username)
                .doOnNext(aBoolean -> {
                    if (aBoolean) throw BaseExceptionUtils.badRequest(username + "名字已存在！");
                })
                .then(update));
    }

    public Mono<Long> deleteAdminUser(Long id) {
        return DbUtils.deleteById(template, id, SystemUser.class);
    }

    public Mono<Page<SystemUser>> getAdminUserPage(SystemUser user, Pageable pageable) {
        Criteria criteria = DbUtils.where()
            .setCriteria(where("is_deleted").is(0))
            .andIf(() -> StringUtils.hasLength(user.getUsername()),
                "username", step -> step.like(user.getUsername() + "%"))
            .criteria();
        return DbUtils.selectPage(template, criteria, pageable, SystemUser.class);
    }

    public Mono<Long> updateAdminPassword(PasswordUpdateDto body) {
        Long adminId = body.getAdminId();
        return DbUtils.findById(template, adminId, SystemUser.class)
            .doOnNext(systemUser -> {
                String old = DigestUtils.md5DigestAsHex(body.getOld().getBytes());
                if (!old.equals(systemUser.getPassword())) {
                    throw BaseExceptionUtils.badRequest("请确认你的密码");
                }
            })
            .then(template.update(SystemUser.class)
                .matching(DbUtils.idQuery(adminId))
                .apply(update("password", DigestUtils.md5DigestAsHex(body.getPassword().getBytes()))));
    }


    public Mono<Long> getRequiredCurrentUserId() {
        return AuthReactorHolder.deferAuthContext()
            .flatMap(context -> Mono.just(context.user().id()))
            .map(Long::parseLong);
    }

    public Mono<String> refreshToken(Long userId) {
        String token = authServer.builder()
            .setSubject(String.valueOf(userId))
            .setAudience("client")
            .build();
        return Mono.just(token);
    }
}
