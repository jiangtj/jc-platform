package com.jiangtj.platform.system;

import com.jiangtj.platform.auth.KeyUtils;
import com.jiangtj.platform.auth.servlet.AuthHolder;
import com.jiangtj.platform.spring.cloud.AuthServer;
import com.jiangtj.platform.spring.cloud.server.ServerContextImpl;
import com.jiangtj.platform.spring.cloud.system.Role;
import com.jiangtj.platform.spring.cloud.system.RoleEndpoint;
import com.jiangtj.platform.system.dto.RoleDto;
import com.jiangtj.platform.system.jooq.tables.SystemRoleCreator;
import com.jiangtj.platform.system.jooq.tables.records.SystemRoleCreatorRecord;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.Stream;

import static com.jiangtj.platform.system.jooq.Tables.SYSTEM_ROLE;
import static com.jiangtj.platform.system.jooq.Tables.SYSTEM_ROLE_CREATOR;

@Slf4j
@Service
public class RoleService {

    @Resource
    private DSLContext create;
    @Resource
    DiscoveryClient discoveryClient;
    @Value("${spring.application.name}")
    String selfName;
    @Resource
    RoleEndpoint roleEndpoint;
    @Resource
    RestClient.Builder loadBalancedClient;
    @Resource
    AuthServer authServer;

    @Getter
    List<ServerRole> serverRoles;

    public void registerRole(RoleDto role) {
        if (AuthHolder.getAuthContext() instanceof ServerContextImpl sctx) {
            String key = KeyUtils.toKey(role.getKey());
            create.insertInto(SYSTEM_ROLE, SYSTEM_ROLE.KEY)
                .values(key)
                .execute();
            new SystemRoleCreator();
            SystemRoleCreatorRecord record = create.newRecord(SYSTEM_ROLE_CREATOR);
            record.setRoleKey(key);
            record.setCreator(sctx.getIssuer());
            record.setName(role.getName());
            record.setAlias(role.getKey());
            record.setAutoCreate(role.getAutoCreate());
            record.store();
        } else {
            // not support
        }
    }

    public Stream<ServerRole> getServerRoleKeysStream() {
        return serverRoles.stream()
            .map(sr -> {
                List<String> list = sr.roles().stream().map(KeyUtils::toKey).toList();
                return new ServerRole(sr.server(), list);
            });
    }

    public List<ServerRole> getServerRoleKeys() {
        return getServerRoleKeysStream().toList();
    }

    public List<Role> getRoleInfo(String key) {
        return getServerRoleKeysStream()
            .filter(sr -> sr.roles().contains(key))
            .map(sr -> loadBalancedClient.build().get()
                .uri("http://" + sr.server() + "/actuator/role/" + key)
                .retrieve()
                .body(Role.class))
            .toList();
                /*.onErrorResume(WebClientResponseException.class, e -> {
                    ProblemDetail detail = e.getResponseBodyAs(ProblemDetail.class);
                    if (detail == null) {
                        detail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    return Mono.error(new BaseException(detail));
                }))*/
    }

}
