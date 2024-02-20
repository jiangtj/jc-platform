package com.jiangtj.platform.spring.cloud.jwt;

import org.springframework.beans.factory.ObjectProvider;

import java.util.List;

public class TokenMutateService {
    ObjectProvider<TokenMutator> op;
    List<TokenMutator> mutators;

    public TokenMutateService(ObjectProvider<TokenMutator> mutatorsOP) {
        this.op = mutatorsOP;
    }

    public String mutate(JwtAuthContext ctx, String target) {
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
