package com.jiangtj.platform.sql.jooq;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Table;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DAOImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public abstract class PageDAOImpl<R extends UpdatableRecord<R>, P, T> extends DAOImpl<R, P, T> {
    protected PageDAOImpl(Table<R> table, Class<P> type) {
        super(table, type);
    }

    protected PageDAOImpl(Table<R> table, Class<P> type, Configuration configuration) {
        super(table, type, configuration);
    }

    public Page<P> fetchPage(Pageable pageable, Condition... conditions) {
        return PageUtils.selectFrom(ctx(), getTable())
            .conditions(conditions)
            .pageable(pageable)
            .fetchPage(getType());
    }
}
