package com.jtj.cloud.common.reactive.db;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

import static org.springframework.data.relational.core.query.Query.query;

@AllArgsConstructor
public class PageQueryBuilder<T,R> {

    private final R2dbcEntityTemplate template;
    private final Class<T> clz;
    private Criteria criteria;
    private Pageable pageable;
    private Function<List<T>, Mono<List<R>>> handler;

    public PageQueryBuilder(R2dbcEntityTemplate template, Class<T> clz, Function<List<T>, Mono<List<R>>> handler) {
        this(template, clz, null, null, handler);
    }

    public PageQueryBuilder<T,R> where(Criteria criteria) {
        this.criteria = criteria;
        return this;
    }

    public PageQueryBuilder<T,R> pageable(Pageable pageable) {
        this.pageable = pageable;
        return this;
    }

    public <X> PageQueryBuilder<T,X> handler(Function<List<T>, Mono<List<X>>> handler) {
        return new PageQueryBuilder<>(template, clz, criteria, pageable, handler);
    }

    public Mono<Page<R>> apply() {
        return Mono.zip(selectList().flatMap(handler), selectCount())
            .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }

    public <X> Mono<Page<X>> apply(Function<List<T>, Mono<List<X>>> handler) {
        return handler(handler).apply();
    }

    private Mono<List<T>> selectList() {
        return template.select(clz)
            .matching(query(criteria).with(pageable))
            .all()
            .collectList();
    }

    private Mono<Long> selectCount() {
        return template.select(clz)
            .matching(query(criteria))
            .count();
    }
}
