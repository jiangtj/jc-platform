package com.jiangtj.platform.spring.cloud.client;

import com.jiangtj.platform.spring.cloud.jwt.JwtAuthContext;

/**
 * Generate new token from an origin token.
 */
public interface TokenMutator {

    boolean support(JwtAuthContext ctx);

    String mutate(JwtAuthContext ctx, String target);

}
