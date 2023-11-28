package com.jiangtj.platform.auth.system;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;

import java.util.List;
import java.util.Objects;

@Endpoint(id = "role")
public class RoleEndpoint {

    private final SystemRoleProvider systemRoleProvider;

    public RoleEndpoint(SystemRoleProvider systemRoleProvider) {
        this.systemRoleProvider = systemRoleProvider;
    }

    @ReadOperation
    public List<String> roles() {
        return systemRoleProvider.getRoles();
    }

    @ReadOperation
    public Role info(@Selector String key) {
        Role role = systemRoleProvider.getRole(key);
        Objects.requireNonNull(role);
        return role;
    }

}
