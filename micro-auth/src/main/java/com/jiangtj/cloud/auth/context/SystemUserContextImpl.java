package com.jiangtj.cloud.auth.context;

import com.jiangtj.cloud.auth.UserClaims;
import com.jiangtj.cloud.auth.rbac.RoleProvider;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class SystemUserContextImpl implements RoleAuthContext {
    private final String token;
    private final Claims claims;
    private final UserClaims user;
    private final RoleProvider roleProvider;

    public SystemUserContextImpl(String token, Claims claims, RoleProvider roleProvider) {
        this.token = token;
        this.claims = claims;
        this.roleProvider = roleProvider;
        String subject = claims.getSubject();
        List<String> roleList = Optional.ofNullable(claims.get("role", String.class))
            .map(r -> r.split(","))
            .map(Arrays::asList)
            .orElse(Collections.emptyList());
        this.user = new UserClaims(subject, roleList);
    }

    @Override
    public List<String> roles() {
        List<String> roles = user.roles();
        if (getId() == 1 && !roles.contains("system")) {
            log.warn("ID:1 is super role, must have system role, but don't have now, please add it.");
            List<String> roleArr = new ArrayList<>(roles);
            roleArr.add("system");
            return roleArr;
        }
        return roles;
    }

    public long getId() {
        return Long.parseLong(user.id());
    }

    public UserClaims user() {
        return user;
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
