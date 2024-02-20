package com.jiangtj.platform.spring.boot.reactive;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

class IntervalSchedulerTest {

    private IntervalScheduler scheduler;

    @Test
    public void shouldRunTenTimes() throws InterruptedException {
        AtomicInteger atomic = new AtomicInteger(0);
        scheduler = new IntervalScheduler("test", (i) -> {
            atomic.incrementAndGet();
            return Mono.empty();
        }, Duration.ofMillis(100));
        scheduler.start();
        Thread.sleep(1099);
        Assertions.assertEquals(10, atomic.getAcquire());
    }


    @AfterEach
    public void tearDown() {
        if (this.scheduler != null) {
            this.scheduler.stop();
        }
    }

}