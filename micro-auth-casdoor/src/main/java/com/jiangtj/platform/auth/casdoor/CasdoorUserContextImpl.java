package com.jiangtj.platform.auth.casdoor;

import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.auth.context.Authorization;
import com.jiangtj.platform.auth.context.DefaultAuthContext;
import com.jiangtj.platform.auth.context.Subject;
import lombok.Getter;
import org.casbin.casdoor.entity.User;

import java.util.List;

@Getter
public class CasdoorUserContextImpl extends DefaultAuthContext {

    private final User casdoorUser;

    public CasdoorUserContextImpl(User user) {
        super(new Subject());
        this.casdoorUser = user;
        Subject subject = this.subject();
        subject.setId(user.id);
        subject.setName(user.name);
        subject.setDisplayName(user.displayName);
        subject.setAvatar(user.avatar);
    }
}
