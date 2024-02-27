package com.jiangtj.platform.core.pk;

import io.jsonwebtoken.security.PublicJwk;
import lombok.Builder;
import lombok.Data;

import java.net.URI;
import java.security.PublicKey;
import java.time.Instant;

@Data
@Builder
public class MicroServiceData {
    private String server;
    private String instanceId;
    private URI uri;
    private PublicJwk<PublicKey> key;
    private Instant instant;
    private Status status;

    enum Status {
        Up, Waiting, Down, Fail
    }
}
