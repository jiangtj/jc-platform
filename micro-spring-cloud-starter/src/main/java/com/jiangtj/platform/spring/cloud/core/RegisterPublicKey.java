package com.jiangtj.platform.spring.cloud.core;

import lombok.Data;

@Data
public class RegisterPublicKey {
    private String application;
    private String jwk;
}
