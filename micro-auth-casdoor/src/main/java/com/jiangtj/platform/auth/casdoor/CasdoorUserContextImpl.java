package com.jiangtj.platform.auth.casdoor;

import com.jiangtj.platform.auth.context.AuthContext;
import lombok.Getter;
import org.casbin.casdoor.entity.User;

import java.util.List;

public class CasdoorUserContextImpl implements AuthContext {
    @Getter
    private final User casdoorUser;

    public CasdoorUserContextImpl(User user) {
        this.casdoorUser = user;
    }

    @Override
    public boolean isLogin() {
        return true;
    }

    @Override
    public String subject() {
        return casdoorUser.id;
    }

    @Override
    public List<String> roles() {
        return List.of();
    }

    @Override
    public List<String> permissions() {
        return List.of();
    }
}
