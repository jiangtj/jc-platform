package com.jtj.cloud.test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public interface TxStepVerifier extends StepVerifier {

    static <T> FirstStep<T> withRollback(final Mono<T> publisher) {
        return StepVerifier.create(publisher.as(TestTransaction::withRollback));
    }

    static <T> FirstStep<T> withRollback(final Flux<T> publisher) {
        return StepVerifier.create(publisher.as(TestTransaction::withRollback));
    }
}
