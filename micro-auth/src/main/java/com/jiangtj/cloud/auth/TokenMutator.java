package com.jiangtj.cloud.auth;

import com.jiangtj.cloud.auth.context.AuthContext;

/**
 * Generate new token from an origin token.
 */
public interface TokenMutator {

    boolean support(AuthContext ctx);

    String mutate(AuthContext ctx, String target);

}
