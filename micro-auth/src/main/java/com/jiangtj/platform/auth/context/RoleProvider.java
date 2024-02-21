package com.jiangtj.platform.auth.context;

import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public interface RoleProvider {

    /**
     * Get role's permission
     * if role is null, return emptyList
     */
    List<String> getPermissions(String key);

    static Stream<String> getPermissionKeyStream(RoleProvider provider, String... roles) {
        return Arrays.stream(roles)
            .map(provider::getPermissions)
            .flatMap(Collection::stream);
    }

    static Stream<String> getPermissionKeyStream(RoleProvider provider, List<String> roles) {
        return roles.stream()
            .map(provider::getPermissions)
            .flatMap(Collection::stream);
    }

    static List<String> getPermissionKeys(RoleProvider provider, String... roles) {
        return getPermissionKeyStream(provider, roles).toList();
    }

    static List<String> getPermissionKeys(RoleProviderAuthContext context) {
        Stream<String> permissionStream = getPermissionKeyStream(context.roleProvider(), context.roles());
        if (CollectionUtils.isEmpty(context.specialPermissions())) {
            return permissionStream.toList();
        }
        return Stream.concat(permissionStream, context.specialPermissions().stream())
            .distinct()
            .toList();
    }

}
