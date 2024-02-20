/*
 * This file is generated by jOOQ.
 */
package com.jiangtj.platform.system.jooq.tables;


import com.jiangtj.platform.system.jooq.Indexes;
import com.jiangtj.platform.system.jooq.Keys;
import com.jiangtj.platform.system.jooq.SystemDb;
import com.jiangtj.platform.system.jooq.tables.records.SystemOperateRecordRecord;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SystemOperateRecord extends TableImpl<SystemOperateRecordRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>system-db.system_operate_record</code>
     */
    public static final SystemOperateRecord SYSTEM_OPERATE_RECORD = new SystemOperateRecord();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SystemOperateRecordRecord> getRecordType() {
        return SystemOperateRecordRecord.class;
    }

    /**
     * The column <code>system-db.system_operate_record.id</code>.
     */
    public final TableField<SystemOperateRecordRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>system-db.system_operate_record.create_time</code>.
     */
    public final TableField<SystemOperateRecordRecord, LocalDateTime> CREATE_TIME = createField(DSL.name("create_time"), SQLDataType.LOCALDATETIME(0).nullable(false), this, "");

    /**
     * The column <code>system-db.system_operate_record.operator</code>.
     */
    public final TableField<SystemOperateRecordRecord, Long> OPERATOR = createField(DSL.name("operator"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>system-db.system_operate_record.content</code>.
     */
    public final TableField<SystemOperateRecordRecord, String> CONTENT = createField(DSL.name("content"), SQLDataType.VARCHAR(256).nullable(false), this, "");

    private SystemOperateRecord(Name alias, Table<SystemOperateRecordRecord> aliased) {
        this(alias, aliased, null);
    }

    private SystemOperateRecord(Name alias, Table<SystemOperateRecordRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>system-db.system_operate_record</code> table
     * reference
     */
    public SystemOperateRecord(String alias) {
        this(DSL.name(alias), SYSTEM_OPERATE_RECORD);
    }

    /**
     * Create an aliased <code>system-db.system_operate_record</code> table
     * reference
     */
    public SystemOperateRecord(Name alias) {
        this(alias, SYSTEM_OPERATE_RECORD);
    }

    /**
     * Create a <code>system-db.system_operate_record</code> table reference
     */
    public SystemOperateRecord() {
        this(DSL.name("system_operate_record"), null);
    }

    public <O extends Record> SystemOperateRecord(Table<O> child, ForeignKey<O, SystemOperateRecordRecord> key) {
        super(child, key, SYSTEM_OPERATE_RECORD);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : SystemDb.SYSTEM_DB;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.SYSTEM_OPERATE_RECORD_IDX_OPERATOR);
    }

    @Override
    public Identity<SystemOperateRecordRecord, Long> getIdentity() {
        return (Identity<SystemOperateRecordRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<SystemOperateRecordRecord> getPrimaryKey() {
        return Keys.KEY_SYSTEM_OPERATE_RECORD_PRIMARY;
    }

    @Override
    public SystemOperateRecord as(String alias) {
        return new SystemOperateRecord(DSL.name(alias), this);
    }

    @Override
    public SystemOperateRecord as(Name alias) {
        return new SystemOperateRecord(alias, this);
    }

    @Override
    public SystemOperateRecord as(Table<?> alias) {
        return new SystemOperateRecord(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public SystemOperateRecord rename(String name) {
        return new SystemOperateRecord(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public SystemOperateRecord rename(Name name) {
        return new SystemOperateRecord(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public SystemOperateRecord rename(Table<?> name) {
        return new SystemOperateRecord(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<Long, LocalDateTime, Long, String> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function4<? super Long, ? super LocalDateTime, ? super Long, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function4<? super Long, ? super LocalDateTime, ? super Long, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
