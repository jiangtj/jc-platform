/*
 * This file is generated by jOOQ.
 */
package com.jiangtj.platform.system.jooq.tables.daos;


import com.jiangtj.platform.sql.jooq.PageUtils;
import com.jiangtj.platform.system.jooq.tables.SystemRoleCreator;
import com.jiangtj.platform.system.jooq.tables.records.SystemRoleCreatorRecord;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Record2;
import org.jooq.impl.DAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Repository
public class SystemRoleCreatorDao extends DAOImpl<SystemRoleCreatorRecord, com.jiangtj.platform.system.jooq.tables.pojos.SystemRoleCreator, Record2<String, String>> {

    /**
     * Create a new SystemRoleCreatorDao without any configuration
     */
    public SystemRoleCreatorDao() {
        super(SystemRoleCreator.SYSTEM_ROLE_CREATOR, com.jiangtj.platform.system.jooq.tables.pojos.SystemRoleCreator.class);
    }

    /**
     * Create a new SystemRoleCreatorDao with an attached configuration
     */
    @Autowired
    public SystemRoleCreatorDao(Configuration configuration) {
        super(SystemRoleCreator.SYSTEM_ROLE_CREATOR, com.jiangtj.platform.system.jooq.tables.pojos.SystemRoleCreator.class, configuration);
    }

    @Override
    public Record2<String, String> getId(com.jiangtj.platform.system.jooq.tables.pojos.SystemRoleCreator object) {
        return compositeKeyRecord(object.roleKey(), object.creator());
    }

    /**
     * Fetch records that have <code>role_key BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemRoleCreator> fetchRangeOfRoleKey(String lowerInclusive, String upperInclusive) {
        return fetchRange(SystemRoleCreator.SYSTEM_ROLE_CREATOR.ROLE_KEY, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>role_key IN (values)</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemRoleCreator> fetchByRoleKey(String... values) {
        return fetch(SystemRoleCreator.SYSTEM_ROLE_CREATOR.ROLE_KEY, values);
    }

    /**
     * Fetch records that have <code>creator BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemRoleCreator> fetchRangeOfCreator(String lowerInclusive, String upperInclusive) {
        return fetchRange(SystemRoleCreator.SYSTEM_ROLE_CREATOR.CREATOR, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>creator IN (values)</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemRoleCreator> fetchByCreator(String... values) {
        return fetch(SystemRoleCreator.SYSTEM_ROLE_CREATOR.CREATOR, values);
    }

    /**
     * Fetch records that have <code>alias BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemRoleCreator> fetchRangeOfAlias(String lowerInclusive, String upperInclusive) {
        return fetchRange(SystemRoleCreator.SYSTEM_ROLE_CREATOR.ALIAS, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>alias IN (values)</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemRoleCreator> fetchByAlias(String... values) {
        return fetch(SystemRoleCreator.SYSTEM_ROLE_CREATOR.ALIAS, values);
    }

    /**
     * Fetch records that have <code>name BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemRoleCreator> fetchRangeOfName(String lowerInclusive, String upperInclusive) {
        return fetchRange(SystemRoleCreator.SYSTEM_ROLE_CREATOR.NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>name IN (values)</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemRoleCreator> fetchByName(String... values) {
        return fetch(SystemRoleCreator.SYSTEM_ROLE_CREATOR.NAME, values);
    }

    /**
     * Fetch records that have <code>auto_create BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemRoleCreator> fetchRangeOfAutoCreate(Byte lowerInclusive, Byte upperInclusive) {
        return fetchRange(SystemRoleCreator.SYSTEM_ROLE_CREATOR.AUTO_CREATE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>auto_create IN (values)</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemRoleCreator> fetchByAutoCreate(Byte... values) {
        return fetch(SystemRoleCreator.SYSTEM_ROLE_CREATOR.AUTO_CREATE, values);
    }

    /**
     * Fetch pages with pageable and conditions.
     */
    public Page<com.jiangtj.platform.system.jooq.tables.pojos.SystemRoleCreator> fetchPage(Pageable pageable, Condition... conditions) {
        return PageUtils.selectFrom(ctx(), getTable())
        .conditions(conditions)
        .pageable(pageable)
        .fetchPage(getType());
    }
}
