package com.jiangtj.platform.system;

import com.jiangtj.platform.auth.KeyUtils;
import com.jiangtj.platform.system.dto.RoleResultDto;
import com.jiangtj.platform.system.entity.SystemUserRole;
import jakarta.annotation.Resource;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Service
public class UserRoleService {

    @Resource
    private R2dbcEntityTemplate template;
    @Resource
    private RoleService roleService;

    public Flux<String> getUserRoles(Long id) {
        return template.select(SystemUserRole.class)
            .matching(query(where("user_id").is(id)))
            .all()
            .map(SystemUserRole::getRole);
    }

    public Flux<String> updateUserRoles(Long id, Flux<String> roles) {
        return deleteUserRoles(id)
            .thenMany(insertUserRoles(id, roles));
    }

    public Flux<String> insertUserRoles(Long id, Flux<String> roles) {
        return roles
            .map(KeyUtils::toKey)
            .map(str -> new SystemUserRole(id, str))
            .flatMap(systemUserRole -> template.insert(systemUserRole))
            .map(SystemUserRole::getRole);
    }

    public Mono<Long> deleteUserRoles(Long id) {
        return template.delete(SystemUserRole.class)
            .matching(query(where("user_id").is(id)))
            .all();
    }

    public Mono<RoleResultDto> toResult(String key) {
        return roleService.getRoleInfo(key)
            .collectList()
            .map(defs -> new RoleResultDto(key, defs));
    }
}
