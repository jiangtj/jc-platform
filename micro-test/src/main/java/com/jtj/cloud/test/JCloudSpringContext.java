package com.jtj.cloud.test;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.reactive.TransactionalOperator;

@Configuration
public class JCloudSpringContext {

    @Bean
    public TestTransaction testTransaction(ObjectProvider<TransactionalOperator> rxtx) {
        return new TestTransaction(rxtx.getIfUnique(() -> null));
    }

}
