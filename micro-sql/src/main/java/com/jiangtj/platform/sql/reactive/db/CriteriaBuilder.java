package com.jiangtj.platform.sql.reactive.db;

import org.springframework.data.relational.core.query.Criteria;

import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class CriteriaBuilder {
    private Criteria criteria;

    public CriteriaBuilder() {
        this.criteria = Criteria.empty();
    }

    public CriteriaBuilder andIf(BooleanSupplier test, String column, Function<Criteria.CriteriaStep, Criteria> doNext) {
        if (test.getAsBoolean()) {
            this.criteria = doNext.apply(this.criteria.and(column));
        }
        return this;
    }

    public CriteriaBuilder setCriteria(Criteria criteria) {
        this.criteria = criteria;
        return this;
    }

    public Criteria criteria() {
        return this.criteria;
    }
}
