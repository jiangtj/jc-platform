package com.jiangtj.platform.sql.jooq;

import com.jiangtj.platform.common.Tuple2;
import org.jooq.Record;
import org.jooq.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.function.BiFunction;

import static org.jooq.impl.DSL.field;

public interface PageUtils {

    static <R extends Record, T> Page<T> selectPage(DSLContext create, Table<R> table, Class<T> pojo, Pageable pageable, Condition... conditions) {
        return selectFrom(create, table)
                .conditions(conditions)
                .pageable(pageable)
                .fetchPage(pojo);
    }

    static <R extends Record> Result<R> selectLimitList(SelectJoinStep<R> joinStep, Pageable pageable, Condition... conditions) {
        SelectConditionStep<R> where = joinStep.where(conditions);
        SelectLimitPercentAfterOffsetStep<R> limited = handlePageable(where, pageable);
        return limited.fetch();
    }

    static Integer selectCount(SelectJoinStep<Record1<Integer>> joinStep, Condition... conditions) {
        return joinStep
                .where(conditions)
                .fetchSingle()
                .value1();
    }

    static <R extends Record> SelectLimitPercentAfterOffsetStep<R> handlePageable(SelectOrderByStep<R> where, Pageable pageable) {
        Sort sort = pageable.getSort();
        if (!sort.isEmpty()) {
            List<SortField<Object>> list = sort.stream()
                    .map(order -> {
                        String property = order.getProperty();
                        if (order.isAscending()) {
                            return field(property).asc();
                        }
                        if (order.isDescending()) {
                            return field(property).desc();
                        }
                        return field(property).sortDefault();
                    })
                    .toList();
            return where.orderBy(list)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize());
        } else {
            return where.offset(pageable.getOffset())
                    .limit(pageable.getPageSize());
        }
    }

    static SelectStep<Record> select(DSLContext create, SelectFieldOrAsterisk... fields) {
        SelectSelectStep<Record> listStep = create.select(fields);
        SelectSelectStep<Record1<Integer>> countStep = create.selectCount();
        return new SelectStep<>(listStep, countStep);
    }

    static <R extends Record> SelectStep<Record1<R>> select(DSLContext create, Table<R> table) {
        SelectSelectStep<Record1<R>> listStep = create.select(table);
        SelectSelectStep<Record1<Integer>> countStep = create.selectCount();
        return new SelectStep<>(listStep, countStep);
    }

    static <R extends Record> FromStep<Record1<R>> selectFrom(DSLContext create, Table<R> table) {
        return select(create, table).from(table);
    }

    static <T> BiFunction<List<T>, Integer, Page<T>> toPage(Pageable pageable) {
        return (list, count) -> new PageImpl<>(list, pageable, count);
    }

    record SelectStep<R extends Record>(SelectSelectStep<R> listStep, SelectSelectStep<Record1<Integer>> countStep) {
        public FromStep<R> from(TableLike<?> table) {
            return new FromStep<>(listStep.from(table), countStep.from(table));
        }
    }

    record FromStep<R extends Record>(SelectJoinStep<R> listStep, SelectJoinStep<Record1<Integer>> countStep) {
        public ConditionStep<R> where(Condition... conditions) {
            return new ConditionStep<>(listStep.where(conditions), countStep.where(conditions));
        }
        public ConditionStep<R> conditions(Condition... conditions) {
            return new ConditionStep<>(listStep.where(conditions), countStep.where(conditions));
        }
        public LimitStep<R> pageable(Pageable pageable) {
            return new LimitStep<>(handlePageable(listStep, pageable), countStep, pageable);
        }
    }

    record ConditionStep<R extends Record>(SelectConditionStep<R> listStep, SelectConditionStep<Record1<Integer>> countStep) {
        public LimitStep<R> pageable(Pageable pageable) {
            return new LimitStep<>(handlePageable(listStep, pageable), countStep, pageable);
        }
    }

    record LimitStep<R extends Record>(SelectLimitPercentAfterOffsetStep<R> listStep,
                                       SelectConnectByStep<Record1<Integer>> countStep,
                                       Pageable pageable) {
        public Tuple2<Result<R>, Integer> fetch() {
            Result<R> result = listStep.fetch();
            Integer count = countStep.fetchSingle().value1();
            return Tuple2.of(result, count);
        }
        public <T> Tuple2<List<T>, Integer> fetchInto(Class<T> clz) {
            List<T> result = listStep.fetchInto(clz);
            Integer count = countStep.fetchSingle().value1();
            return Tuple2.of(result, count);
        }
        public <T> Page<T> fetchPage(Class<T> clz) {
            return fetchInto(clz).map(toPage(pageable));
        }

        public <T> T biSubscribe(BiFunction<SelectLimitPercentAfterOffsetStep<R>, SelectConnectByStep<Record1<Integer>>, T> biFunction) {
            return biFunction.apply(listStep, countStep);
        }
    }

}
