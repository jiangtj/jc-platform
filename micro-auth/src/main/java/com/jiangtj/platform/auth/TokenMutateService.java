package com.jiangtj.platform.auth;

import com.jiangtj.platform.auth.context.AuthContext;
import org.springframework.beans.factory.ObjectProvider;

import java.util.List;

public class TokenMutateService {
    ObjectProvider<TokenMutator> op;
    List<TokenMutator> mutators;

    public TokenMutateService(ObjectProvider<TokenMutator> mutatorsOP) {
        this.op = mutatorsOP;
    }

    public String mutate(AuthContext ctx, String target) {
        if (mutators == null) {
            mutators = op.orderedStream().toList();
        }
        for (TokenMutator mutator : mutators) {
            if (mutator.support(ctx)) {
                return mutator.mutate(ctx, target);
            }
        }
        return ctx.token();
    }

}
