package com.jiangtj.platform.sql.r2dbc.db;

import com.jiangtj.platform.sql.r2dbc.PageUtils;
import com.jiangtj.platform.sql.r2dbc.R2dbcExchangeHolder;
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
public class PageQueryBuilder<T> {

    private final R2dbcEntityTemplate template;
    private final Class<T> clz;
    private Criteria criteria;

    public PageQueryBuilder(R2dbcEntityTemplate template, Class<T> clz) {
        this(template, clz, null);
    }

    public PageQueryBuilder<T> where(Criteria criteria) {
        this.criteria = criteria;
        return this;
    }

    public <R> PageQueryFilterBuilder<T,R> handler(Function<T, Mono<R>> handler) {
        return new PageQueryFilterBuilder<>(template, clz, criteria, handler);
    }

    public Mono<Page<T>> toPage() {
        return deferPageable()
            .flatMap(pageable -> Mono.zip(selectList(pageable), selectCount())
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2())));
    }

    private Mono<Pageable> deferPageable() {
        return R2dbcExchangeHolder.deferExchange()
            .flatMap(exchange -> Mono.just(PageUtils.from(exchange.getRequest())));
    }

    private Mono<List<T>> selectList(Pageable pageable) {
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
