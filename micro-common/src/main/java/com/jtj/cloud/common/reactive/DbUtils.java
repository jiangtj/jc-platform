package com.jtj.cloud.common.reactive;

import com.jtj.cloud.common.BaseExceptionUtils;
import com.jtj.cloud.common.reactive.db.CriteriaBuilder;
import com.jtj.cloud.common.reactive.db.LogicalDelete;
import com.jtj.cloud.common.reactive.db.PageQueryBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.data.relational.core.query.Query.query;

public interface DbUtils {

    List<String> ignoreDescriptors = Arrays.asList("class", "createTime", "modifyTime", "isDeleted");

    static CriteriaBuilder where() {
        return new CriteriaBuilder();
    }

    static <T> PageQueryBuilder<T,T> pageQuery(R2dbcEntityTemplate template, Class<T> clz) {
        return new PageQueryBuilder<>(template, clz, Mono::just);
    }

    static <T> Mono<Page<T>> selectPage(R2dbcEntityTemplate template, Criteria criteria, Pageable pageable, Class<T> clz) {
        return pageQuery(template, clz)
            .where(criteria)
            .pageable(pageable)
            .apply();
    }

    static Query idQuery(Long id) {
        return query(Criteria.where("id").is(id));
    }

    static Criteria notDel() {
        return Criteria.where("is_deleted").is(0);
    }

    static Criteria isDel() {
        return Criteria.where("is_deleted").is(1);
    }

    static <T> Mono<T> findById(R2dbcEntityTemplate template, Long id, Class<T> clz) {
        return template.select(clz).matching(idQuery(id)).one();
    }

    static <T> Mono<Long> deleteById(R2dbcEntityTemplate template, Long id, Class<T> clz) {
        LogicalDelete val = AnnotationUtils.findAnnotation(clz, LogicalDelete.class);
        if (val == null) {
            return template.delete(clz).matching(idQuery(id)).all();
        }
        return template.update(clz)
            .matching(DbUtils.idQuery(id))
            .apply(Update.update(val.value(), val.set()));
    }

    static <T> Mono<T> insert(R2dbcEntityTemplate template, T entity) {
        return template.insert(entity);
    }

    static <T> Mono<Long> update(R2dbcEntityTemplate template, T entity) {
        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(entity.getClass());
        Update update = null;
        Query query = null;
        for (PropertyDescriptor descriptor: descriptors) {
            try {
                String name = descriptor.getName();

                if (ignoreDescriptors.contains(name)) {
                    continue;
                }

                Object invoke = descriptor.getReadMethod().invoke(entity);
                if (invoke == null) {
                    continue;
                }
                if ("id".equals(name)) {
                    query = query(Criteria.where(name).is(invoke));
                } else {
                    update = update == null? Update.update(name, invoke):
                        update.set(name, invoke);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        if (query == null || update == null) {
            return Mono.error(BaseExceptionUtils.internalServerError("无法生成有效的Sql语句!"));
        }

        return template.update(entity.getClass())
            .matching(query)
            .apply(update);
    }

    static <T,R,U> Flux<U> fetch(R2dbcEntityTemplate template, Iterable<T> data, Class<R> clz, Function<T,Long> tId, Function<R,Long> rId, BiFunction<T,R,U> doNext) {
        return fetch(template, data, clz, tId, rId)
            .map(tu -> doNext.apply(tu.getT1(), tu.getT2()));
    }

    static <T,R> Flux<Tuple2<T,R>> fetch(R2dbcEntityTemplate template, Iterable<T> data, Class<R> clz, Function<T,Long> tId, Function<R,Long> rId) {
        return Flux.fromIterable(data)
            .map(tId)
            .distinct()
            .collectList()
            .flatMapMany(ids -> template.select(clz)
                .matching(query(Criteria.where("id").in(ids)))
                .all())
            .collect(Collectors.toMap(rId, Function.identity()))
            .flatMapMany(wrapperMap -> Flux.fromIterable(data)
                .map(item -> {
                    R r = wrapperMap.get(tId.apply(item));
                    return Tuples.of(item, r);
                }));
    }
}
