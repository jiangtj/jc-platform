package com.jiangtj.cloud.auth.rbac;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.DEDUCTION,
    defaultImpl = Permission.PermissionRecord.class)
public interface Permission {

    String name();

    String description();

    static Permission of(String name) {
        return of(name, "");
    }

    static Permission of(String name, String description) {
        return new PermissionRecord(name, description);
    }

    static List<Permission> of(List<String> p) {
        return p.stream()
            .map(Permission::of)
            .toList();
    }

    record PermissionRecord(String name, String description) implements Permission {}

}
