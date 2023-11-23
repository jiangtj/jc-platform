package com.jiangtj.cloud.common.reactive;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.function.Function;
import java.util.logging.Level;

@Slf4j
public class IntervalScheduler {

	private final String name;

	private final Function<Long, Mono<Void>> fn;

	@Getter
	@Setter
	private Duration interval;

	private Disposable subscription;

	private Scheduler scheduler;

	public IntervalScheduler(String name, Function<Long, Mono<Void>> fn) {
		this(name, fn, Duration.ofSeconds(10));
	}

	public IntervalScheduler(String name, Function<Long, Mono<Void>> checkFn, Duration interval) {
		this.name = name;
		this.fn = checkFn;
		this.interval = interval;
	}

	public void start() {
		this.scheduler = Schedulers.newSingle(this.name + "-scheduler");
		this.subscription = Flux.interval(this.interval)
			.doOnSubscribe((s) -> log.debug("Scheduled {}-scheduler every {}", this.name, this.interval))
			.log(log.getName(), Level.FINEST)
			.subscribeOn(this.scheduler)
			.flatMap(this.fn)
			.retryWhen(Retry.backoff(Long.MAX_VALUE, Duration.ofSeconds(1))
				.doBeforeRetry((s) -> log.warn("Unexpected error in {}-scheduler", this.name, s.failure())))
			.subscribe(null, (error) -> log.error("Unexpected error in {}-scheduler", name, error));
	}


	public void stop() {
		if (this.subscription != null) {
			this.subscription.dispose();
			this.subscription = null;
		}
		if (this.scheduler != null) {
			this.scheduler.dispose();
			this.scheduler = null;
		}
	}

}
