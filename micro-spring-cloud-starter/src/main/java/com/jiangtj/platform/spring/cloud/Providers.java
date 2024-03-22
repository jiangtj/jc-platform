package com.jiangtj.platform.spring.cloud;

import com.jiangtj.platform.spring.cloud.jwt.JwtAuthContext;

public interface Providers {
    String KEY= JwtAuthContext.PROVIDER;
    String SERVER= "server";
    String SYSTEM_USER = "system_user";
}
