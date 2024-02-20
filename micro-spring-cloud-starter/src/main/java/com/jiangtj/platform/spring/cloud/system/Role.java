package com.jiangtj.platform.spring.cloud.system;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.DEDUCTION,
    defaultImpl = Role.RoleRecord.class)
public interface Role {

    String name();

    @Nullable
    String description();

    List<Permission> permissions();

    record RoleRecord(String name, String description, List<Permission> permissions) implements Role {}

    static Role of(String name) {
        return of(name, "");
    }

    static Role of(String name, String description) {
        return of(name, description, Collections.emptyList());
    }

    static Role of(String name, String description, List<Permission> permissions) {
        return new RoleRecord(name, description, permissions);
    }

    static Role of(String name, String description, Permission... permissions) {
        return new RoleRecord(name, description, Arrays.asList(permissions));
    }
}
