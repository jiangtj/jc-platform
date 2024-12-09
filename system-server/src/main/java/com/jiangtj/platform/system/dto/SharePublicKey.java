package com.jiangtj.platform.system.dto;

import io.jsonwebtoken.security.PublicJwk;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PublicKey;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharePublicKey {
    private String application;
    private PublicJwk<PublicKey> jwk;
}
