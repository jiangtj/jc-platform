package com.jiangtj.cloud.auth;

public interface AuthRequestAttributes {
    String TOKEN_HEADER_NAME = "Authorization";
    String TOKEN_HEADER_PREFIX = "Bearer ";
    String AUTH_CONTEXT_ATTRIBUTE = "J_PLATFORM_AUTh_ATTRIBUTE";
}
