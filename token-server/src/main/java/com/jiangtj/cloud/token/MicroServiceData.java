package com.jiangtj.cloud.token;

import io.jsonwebtoken.security.PublicJwk;
import lombok.Builder;
import lombok.Data;

import java.net.URI;
import java.security.PublicKey;
import java.time.Instant;

@Data
@Builder
public class MicroServiceData {
    private URI uri;
    private PublicJwk<PublicKey> key;
    private Instant instant;
}
