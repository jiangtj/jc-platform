/*
 * This file is generated by jOOQ.
 */
package com.jiangtj.platform.system.jooq.tables.daos;


import com.jiangtj.platform.sql.jooq.PageUtils;
import com.jiangtj.platform.system.jooq.tables.SystemKeyShare;
import com.jiangtj.platform.system.jooq.tables.records.SystemKeyShareRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
@Repository
public class SystemKeyShareDao extends DAOImpl<SystemKeyShareRecord, com.jiangtj.platform.system.jooq.tables.pojos.SystemKeyShare, String> {

    /**
     * Create a new SystemKeyShareDao without any configuration
     */
    public SystemKeyShareDao() {
        super(SystemKeyShare.SYSTEM_KEY_SHARE, com.jiangtj.platform.system.jooq.tables.pojos.SystemKeyShare.class);
    }

    /**
     * Create a new SystemKeyShareDao with an attached configuration
     */
    @Autowired
    public SystemKeyShareDao(Configuration configuration) {
        super(SystemKeyShare.SYSTEM_KEY_SHARE, com.jiangtj.platform.system.jooq.tables.pojos.SystemKeyShare.class, configuration);
    }

    @Override
    public String getId(com.jiangtj.platform.system.jooq.tables.pojos.SystemKeyShare object) {
        return object.kid();
    }

    /**
     * Fetch records that have <code>kid BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemKeyShare> fetchRangeOfKid(String lowerInclusive, String upperInclusive) {
        return fetchRange(SystemKeyShare.SYSTEM_KEY_SHARE.KID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>kid IN (values)</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemKeyShare> fetchByKid(String... values) {
        return fetch(SystemKeyShare.SYSTEM_KEY_SHARE.KID, values);
    }

    /**
     * Fetch a unique record that has <code>kid = value</code>
     */
    public com.jiangtj.platform.system.jooq.tables.pojos.SystemKeyShare fetchOneByKid(String value) {
        return fetchOne(SystemKeyShare.SYSTEM_KEY_SHARE.KID, value);
    }

    /**
     * Fetch a unique record that has <code>kid = value</code>
     */
    public Optional<com.jiangtj.platform.system.jooq.tables.pojos.SystemKeyShare> fetchOptionalByKid(String value) {
        return fetchOptional(SystemKeyShare.SYSTEM_KEY_SHARE.KID, value);
    }

    /**
     * Fetch records that have <code>jwk BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemKeyShare> fetchRangeOfJwk(String lowerInclusive, String upperInclusive) {
        return fetchRange(SystemKeyShare.SYSTEM_KEY_SHARE.JWK, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>jwk IN (values)</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemKeyShare> fetchByJwk(String... values) {
        return fetch(SystemKeyShare.SYSTEM_KEY_SHARE.JWK, values);
    }

    /**
     * Fetch records that have <code>publish_time BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemKeyShare> fetchRangeOfPublishTime(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(SystemKeyShare.SYSTEM_KEY_SHARE.PUBLISH_TIME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>publish_time IN (values)</code>
     */
    public List<com.jiangtj.platform.system.jooq.tables.pojos.SystemKeyShare> fetchByPublishTime(LocalDateTime... values) {
        return fetch(SystemKeyShare.SYSTEM_KEY_SHARE.PUBLISH_TIME, values);
    }

    /**
     * Fetch pages with pageable and conditions.
     */
    public Page<com.jiangtj.platform.system.jooq.tables.pojos.SystemKeyShare> fetchPage(Pageable pageable, Condition... conditions) {
        return PageUtils.selectFrom(ctx(), getTable())
        .conditions(conditions)
        .pageable(pageable)
        .fetchPage(getType());
    }
}