package com.jtj.cloud.system;

import com.jtj.cloud.auth.AuthServer;
import com.jtj.cloud.common.BaseExceptionUtils;
import com.jtj.cloud.common.reactive.DbUtils;
import com.jtj.cloud.system.dto.PasswordUpdateDto;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;
import static org.springframework.data.relational.core.query.Update.update;

@Service
public class AdminService {

    @Resource
    private AuthServer authServer;

    @Resource
    private R2dbcEntityTemplate template;

    public Mono<AdminUser> login(AdminUser user) {
        String username = user.getUsername();
        String password = user.getPassword();
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
            throw BaseExceptionUtils.badRequest("请输入用户名与密码！");
        }
        return template.select(AdminUser.class)
            .matching(query(where("username").is(username).and(DbUtils.notDel())))
            .one()
            .switchIfEmpty(Mono.error(BaseExceptionUtils.badRequest("用户不存在")))
            .filter(item -> DigestUtils.md5DigestAsHex(password.getBytes()).equals(item.getPassword()))
            .switchIfEmpty(Mono.error(BaseExceptionUtils.badRequest("密码错误！")));
            /*.map(item -> {
                String token = authServer.builder()
                    .setSubject(String.valueOf(item.getId()))
                    .setAudience("client")
                    .build();
                return LoginResultDto.of(item, token);
            });*/
    }

    public Mono<AdminUser> getAdminUser(Long id) {
        return DbUtils.findById(template, id, AdminUser.class);
    }

    public Mono<Boolean> isExistsName(String username) {
        return template.select(AdminUser.class)
            .matching(query(where("username").is(username).and(DbUtils.notDel())))
            .exists();
    }

    public Mono<AdminUser> createAdminUser(AdminUser user) {
        Mono<AdminUser> insert = Mono.just(user)
            .doOnNext(adminUser -> adminUser.setPassword(DigestUtils.md5DigestAsHex(adminUser.getPassword().getBytes())))
            .flatMap(adminUser -> DbUtils.insert(template, adminUser));
        return isExistsName(user.getUsername())
            .doOnNext(isE -> {
                if (isE) throw BaseExceptionUtils.badRequest("不能创建一样的名字！");
            })
            .then(insert);
    }

    public Mono<Long> updateAdminUser(AdminUser user) {
        Long id = user.getId();
        String username = user.getUsername();
        Objects.requireNonNull(id);
        Objects.requireNonNull(username);
        return template.update(AdminUser.class)
            .matching(query(where("id").is(id)))
            .apply(update("username", username))
            .as(update -> isExistsName(username)
                .doOnNext(aBoolean -> {
                    if (aBoolean) throw BaseExceptionUtils.badRequest(username + "名字已存在！");
                })
                .then(update));
    }

    public Mono<Long> deleteAdminUser(Long id) {
        return DbUtils.deleteById(template, id, AdminUser.class);
    }

    public Mono<Page<AdminUser>> getAdminUserPage(AdminUser user, Pageable pageable) {
        Criteria criteria = DbUtils.where()
            .setCriteria(where("is_deleted").is(0))
            .andIf(() -> StringUtils.hasLength(user.getUsername()),
                "username", step -> step.like(user.getUsername() + "%"))
            .criteria();
        return DbUtils.selectPage(template, criteria, pageable, AdminUser.class);
    }

    public Mono<Long> updateAdminPassword(PasswordUpdateDto body) {
        Long adminId = body.getAdminId();
        return DbUtils.findById(template, adminId, AdminUser.class)
            .doOnNext(adminUser -> {
                String old = DigestUtils.md5DigestAsHex(body.getOld().getBytes());
                if (!old.equals(adminUser.getPassword())) {
                    throw BaseExceptionUtils.badRequest("请确认你的密码");
                }
            })
            .then(template.update(AdminUser.class)
                .matching(DbUtils.idQuery(adminId))
                .apply(update("password", DigestUtils.md5DigestAsHex(body.getPassword().getBytes()))));
    }

    public Long getRequiredCurrentUserId(ServerRequest request) {
        return getCurrentUserId(request).orElseThrow(() -> BaseExceptionUtils.internalServerError("无法获取当前的用户ID"));
    }

    public Optional<Long> getCurrentUserId(ServerRequest request) {
        return getCurrentClaims(request)
            .map(claims -> Long.valueOf(claims.getSubject()));
    }

    public Optional<Claims> getCurrentClaims(ServerRequest request) {
        ServerWebExchange exchange = request.exchange();
        return Optional.ofNullable(exchange.getAttribute("user-claims"));
    }

    public Mono<String> refreshToken(Long userId) {
        String token = authServer.builder()
            .setSubject(String.valueOf(userId))
            .setAudience("client")
            .build();
        return Mono.just(token);
    }
}
