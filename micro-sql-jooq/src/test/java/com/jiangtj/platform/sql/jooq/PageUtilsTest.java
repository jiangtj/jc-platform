package com.jiangtj.platform.sql.jooq;

import com.jiangtj.platform.sql.jooq.jooq.tables.pojos.SystemUser;
import com.jiangtj.platform.sql.jooq.jooq.tables.records.SystemUserRecord;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultDSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.jiangtj.platform.sql.jooq.jooq.tables.SystemUser.SYSTEM_USER;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.field;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class PageUtilsTest {

    private final DSLContext create = new DefaultDSLContext(SQLDialect.MYSQL);

    @Test
    void selectPage() {
        /*DSLContext create = new DefaultDSLContext(SQLDialect.MYSQL);
        Pageable pageable = PageRequest.of(1, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<SystemUser> systemUsers = PageUtils.selectPage(create, SYSTEM_USER, SystemUser.class, pageable);*/
    }

    @Test
    void selectLimitList() {
    }

    @Test
    void selectCount() {
    }

    @Test
    void selectFieldStep() {
        PageUtils.SelectStep<Record> step = PageUtils.select(create, field("s"));
        log.info(step.listStep().getSQL());
        log.info(step.countStep().getSQL());
    }

    @Test
    void fromStep() {
        PageUtils.FromStep<Record1<SystemUserRecord>> step = PageUtils.selectFrom(create, SYSTEM_USER);
        log.info(step.listStep().getSQL());
        log.info(step.countStep().getSQL());
    }

    @Test
    void conditionStep() {
        SystemUser user = new SystemUser(1L, null, null, null);
        PageUtils.ConditionStep<Record1<SystemUserRecord>> step = PageUtils.selectFrom(create, SYSTEM_USER)
                .conditions(condition(new SystemUserRecord(user)));
        log.info(step.listStep().getSQL());
        log.info(step.countStep().getSQL());
    }

    @Test
    void limitStep() {
        SystemUser user = new SystemUser(1L, null, null, null);
        Pageable pageable = PageRequest.of(1, 10, Sort.by(Sort.Direction.DESC, "id"));
        PageUtils.LimitStep<Record1<SystemUserRecord>> step = PageUtils.selectFrom(create, SYSTEM_USER)
                .conditions(condition(new SystemUserRecord(user)))
                .pageable(pageable);
        log.info(step.listStep().getSQL());
        List<Object> listValues = step.listStep().getBindValues();
        assertEquals(3, listValues.size());
        log.info(step.countStep().getSQL());
        List<Object> countValues = step.countStep().getBindValues();
        assertEquals(1, countValues.size());
    }
}