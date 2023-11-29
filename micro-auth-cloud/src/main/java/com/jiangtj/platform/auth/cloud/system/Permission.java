package com.jiangtj.platform.auth.cloud.system;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.lang.Nullable;

import java.util.List;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.DEDUCTION,
    defaultImpl = Permission.PermissionRecord.class)
public interface Permission {

    String name();

    @Nullable
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
