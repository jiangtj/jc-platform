package com.jiangtj.cloud.auth.reactive;

import lombok.Data;

@Data
public class JwtHeader {
    private String kid;
    private String alg;
}
