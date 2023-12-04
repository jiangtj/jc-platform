package com.jiangtj.platform.auth.context;

import org.springframework.http.HttpRequest;
import org.springframework.lang.Nullable;

public interface AuthContextConverter {

    @Nullable
    AuthContext convert(HttpRequest request);

}
