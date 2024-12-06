package com.jiangtj.platform.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.jsonwebtoken.security.PublicJwk;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.security.PublicKey;
import java.time.LocalDateTime;

@Data
public class SharePublicKey {
    @Id
    private String kid;
    private PublicJwk<PublicKey> jwk;
}
