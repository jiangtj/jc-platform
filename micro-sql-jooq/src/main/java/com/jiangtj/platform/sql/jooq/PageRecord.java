package com.jiangtj.platform.sql.jooq;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public record PageRecord<R extends Record>(Result<R> list, Integer count) {
    public <T> Page<T> toPage(Class<T> t, Pageable pageable) {
        List<T> into = list.into(t);
        return new PageImpl<>(into, pageable, count);
    }
}
