package com.jiangtj.platform.auth;

import com.jiangtj.platform.auth.context.JwtAuthContext;

/**
 * Generate new token from an origin token.
 */
public interface TokenMutator {

    boolean support(JwtAuthContext ctx);

    String mutate(JwtAuthContext ctx, String target);

}
