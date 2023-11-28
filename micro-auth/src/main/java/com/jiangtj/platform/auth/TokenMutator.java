package com.jiangtj.platform.auth;

import com.jiangtj.platform.auth.context.AuthContext;

/**
 * Generate new token from an origin token.
 */
public interface TokenMutator {

    boolean support(AuthContext ctx);

    String mutate(AuthContext ctx, String target);

}
