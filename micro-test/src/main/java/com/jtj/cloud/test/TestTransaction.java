package com.jtj.cloud.test;

import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TestTransaction {

    private static TransactionalOperator rxtx;

    public TestTransaction(TransactionalOperator rxtx) {
        TestTransaction.rxtx = rxtx;
    }

    public static <T> Mono<T> withRollback(Mono<T> publisher) {
        return rxtx.execute(tx -> {
                tx.setRollbackOnly();
                return publisher;
            })
            .next();
    }

    public static <T> Flux<T> withRollback(Flux<T> publisher) {
        return rxtx.execute(tx -> {
            tx.setRollbackOnly();
            return publisher;
        });
    }
}
