package com.jiangtj.platform.system;

import com.jiangtj.platform.auth.reactive.AuthReactorHolder;
import com.jiangtj.platform.sql.jooq.PageUtils;
import com.jiangtj.platform.sql.r2dbc.DbUtils;
import com.jiangtj.platform.system.dto.LoginDto;
import com.jiangtj.platform.system.dto.LoginResultDto;
import com.jiangtj.platform.system.dto.PasswordUpdateDto;
import com.jiangtj.platform.system.entity.SystemUser;
import com.jiangtj.platform.web.BaseExceptionUtils;
import jakarta.annotation.Resource;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.jiangtj.platform.system.jooq.Tables.SYSTEM_USER;
import static org.jooq.impl.DSL.noCondition;
import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;
import static org.springframework.data.relational.core.query.Update.update;

@Service
public class UserService {

    @Resource
    private R2dbcEntityTemplate template;
    @Resource
    private DSLContext create;
    @Resource
    private UserRoleService userRoleService;

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
            .doOnNext(item -> {
                if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(item.getPassword())) {
                    throw BaseExceptionUtils.badRequest("密码错误！");
                }
            })
            .flatMap(item -> {
                Long id = item.getId();
                return userRoleService.getUserRoles(id)
                    .collectList()
                    .map(roles -> LoginResultDto.of(item, roles));
            });
    }

    public Mono<SystemUser> getAdminUser(Long id) {
        return DbUtils.findById(template, id, SystemUser.class);
    }

    public Mono<SystemUser> isExistsName(SystemUser user) {
        return template.select(SystemUser.class)
            .matching(query(where("username").is(user.getUsername()).and(DbUtils.notDel())))
            .exists()
            .doOnNext(isE -> {
                if (isE) throw BaseExceptionUtils.badRequest("不能创建一样的名字！");
            })
            .thenReturn(user);
    }

    public Mono<SystemUser> createAdminUser(SystemUser user) {
        return Mono.just(user)
            .flatMap(this::isExistsName)
            .doOnNext(systemUser -> systemUser.setPassword(DigestUtils.md5DigestAsHex(systemUser.getPassword().getBytes())))
            .flatMap(systemUser -> DbUtils.insert(template, systemUser));
    }

    public Mono<SystemUser> updateAdminUser(SystemUser user) {
        return Mono.just(user)
            .doOnNext(u1 -> {
                Long id = u1.getId();
                String username = u1.getUsername();
                Objects.requireNonNull(id);
                Objects.requireNonNull(username);
            })
            .flatMap(this::isExistsName)
            .flatMap(u1 -> template.update(SystemUser.class)
                .matching(query(where("id").is(u1.getId())))
                .apply(update("username", u1.getUsername())))
            .then(template.select(SystemUser.class).matching(query(where("id").is(user.getId()))).one());
    }

    public Mono<Long> deleteAdminUser(Long id) {
        return DbUtils.deleteById(template, id, SystemUser.class);
    }

    public Mono<Page<SystemUser>> getAdminUserPage(SystemUser user, Pageable pageable) {
        /*Criteria criteria = DbUtils.where()
            .setCriteria(where("is_deleted").is(0))
            .andIf(() -> StringUtils.hasLength(user.getUsername()),
                "username", step -> step.like(user.getUsername() + "%"))
            .criteria();
        return DbUtils.selectPage(template, SystemUser.class, criteria);*/
        // try use jooq query
        return PageUtils.selectFrom(create, SYSTEM_USER)
            .conditions(SYSTEM_USER.IS_DELETED.eq((byte) 0)
                .and(StringUtils.hasLength(user.getUsername()) ?
                    SYSTEM_USER.USERNAME.like(user.getUsername() + "%") :
                    noCondition()))
            .pageable(pageable)
            .biSubscribe((listS, countS) -> Mono.zip(
                Flux.from(listS).map(l -> l.into(SystemUser.class)).collectList(),
                Mono.from(countS).map(Record1::value1)))
            .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
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
            .flatMap(context -> Mono.just(context.subject()))
            .map(Long::parseLong);
    }
}
