package com.jiangtj.platform.auth.cloud.system;

import com.jiangtj.platform.auth.context.JwtAuthContext;
import com.jiangtj.platform.auth.context.RoleProvider;
import com.jiangtj.platform.auth.context.RoleProviderAuthContext;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class SystemUserContextImpl implements RoleProviderAuthContext, JwtAuthContext {
    private final String token;
    private final Claims claims;
    private final List<String> roles;
    private final RoleProvider roleProvider;

    public SystemUserContextImpl(String token, Claims claims, RoleProvider roleProvider) {
        this.token = token;
        this.claims = claims;
        this.roleProvider = roleProvider;
        List<String> roleList = new ArrayList<>(Optional.ofNullable(claims.get("role", String.class))
                .map(r -> r.split(","))
                .map(Arrays::asList)
                .orElse(Collections.emptyList()));
        if (getId() == 1 && !roleList.contains("system")) {
            roleList.add("system");
        }
        this.roles = roleList;
    }

    @Override
    public List<String> roles() {
        return roles;
    }

    public long getId() {
        return Long.parseLong(claims().getSubject());
    }

    @Override
    public RoleProvider roleProvider() {
        return roleProvider;
    }

    @Override
    public String token() {
        return token;
    }

    @Override
    public Claims claims() {
        return claims;
    }

}
