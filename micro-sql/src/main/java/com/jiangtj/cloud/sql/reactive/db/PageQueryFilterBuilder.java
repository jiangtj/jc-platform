package com.jiangtj.cloud.sql.reactive.db;

import com.jiangtj.cloud.common.reactive.JCloudReactorHolder;
import com.jiangtj.cloud.sql.reactive.PageUtils;
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
public class PageQueryFilterBuilder<T,R> {

    private final R2dbcEntityTemplate template;
    private final Class<T> clz;
    private Criteria criteria;
    private Function<T, Mono<R>> flatMapFn;

    public PageQueryFilterBuilder(R2dbcEntityTemplate template, Class<T> clz, Function<T, Mono<R>> handler) {
        this(template, clz, null, handler);
    }

    public PageQueryFilterBuilder<T,R> where(Criteria criteria) {
        this.criteria = criteria;
        return this;
    }

    public <X> PageQueryFilterBuilder<T,X> handler(Function<T, Mono<X>> handler) {
        return new PageQueryFilterBuilder<>(template, clz, criteria, handler);
    }

    public Mono<Page<R>> toPage() {
        return deferPageable()
            .flatMap(pageable -> Mono.zip(selectList(pageable), selectCount())
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2())));
    }

    private Mono<Pageable> deferPageable() {
        return JCloudReactorHolder.deferExchange()
            .flatMap(exchange -> Mono.just(PageUtils.from(exchange.getRequest())));
    }

    private Mono<List<R>> selectList(Pageable pageable) {
        return template.select(clz)
            .matching(query(criteria).with(pageable))
            .all()
            .flatMap(flatMapFn)
            .collectList();
    }

    private Mono<Long> selectCount() {
        return template.select(clz)
            .matching(query(criteria))
            .count();
    }
}
