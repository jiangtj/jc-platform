package com.jiangtj.platform.system.dto;

import io.jsonwebtoken.security.PublicJwk;
import lombok.Data;

import java.security.PublicKey;

@Data
public class SharePublicKey {
    private String application;
    private PublicJwk<PublicKey> jwk;
}
