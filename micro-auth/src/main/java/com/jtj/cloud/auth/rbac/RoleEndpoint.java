package com.jtj.cloud.auth.rbac;

import com.jtj.cloud.auth.AuthUtils;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;

import java.util.List;
import java.util.Objects;

@Endpoint(id = "role")
public class RoleEndpoint {

    private final RoleContext context;
    private final static Role unknownRole = Role.of("UNKNOWN");

    public RoleEndpoint(RoleContext context) {
        this.context = context;
    }

    @ReadOperation
    public List<String> roles() {
        return context.getRoles().stream()
            .map(Role::toString)
            .map(String::toLowerCase)
            .toList();
    }

    @ReadOperation
    public Role info(@Selector String name) {
        Objects.requireNonNull(name);
        return context.getRoles().stream()
            .filter(role -> AuthUtils.toKey(name).equals(role.key()))
            .findFirst()
            .orElse(unknownRole);
    }

}
