package com.jtj.cloud.auth.rbac;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Endpoint(id = "role")
public class RoleEndpoint {

    private final RoleProvider roleProvider;

    public RoleEndpoint(RoleProvider roleProvider) {
        this.roleProvider = roleProvider;
    }

    @ReadOperation
    public List<String> roles() {
        return roleProvider.getRoles();
    }

    @ReadOperation
    public Role info(@Selector String key) {
        Role role = roleProvider.getRole(key);
        Objects.requireNonNull(role);
        List<Permission> permissions = roleProvider.getPermissions(key);
        if (CollectionUtils.isEmpty(permissions)) {
            return Role.of(role.name(), role.description(), roleProvider.getPermissions(key));
        }
        return role;
    }

}
