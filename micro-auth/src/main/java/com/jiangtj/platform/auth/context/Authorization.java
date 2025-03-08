package com.jiangtj.platform.auth.context;

import java.util.List;

public interface Authorization {
    List<String> roles();
    List<String> permissions();

    static Authorization empty() {
        return DefaultAuthorization.EMPTY;
    }

    static Authorization create(List<String> roles) {
        return new DefaultAuthorization(roles);
    }

    static Authorization create(List<String> roles, List<String> permissions) {
        return new DefaultAuthorization(roles, permissions);
    }
}
