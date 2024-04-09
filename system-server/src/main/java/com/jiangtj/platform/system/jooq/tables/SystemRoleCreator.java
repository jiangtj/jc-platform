/*
 * This file is generated by jOOQ.
 */
package com.jiangtj.platform.system.jooq.tables;


import com.jiangtj.platform.system.jooq.Keys;
import com.jiangtj.platform.system.jooq.SystemDb;
import com.jiangtj.platform.system.jooq.tables.records.SystemRoleCreatorRecord;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.util.function.Function;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SystemRoleCreator extends TableImpl<SystemRoleCreatorRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>system-db.system_role_creator</code>
     */
    public static final SystemRoleCreator SYSTEM_ROLE_CREATOR = new SystemRoleCreator();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SystemRoleCreatorRecord> getRecordType() {
        return SystemRoleCreatorRecord.class;
    }

    /**
     * The column <code>system-db.system_role_creator.role_key</code>.
     */
    public final TableField<SystemRoleCreatorRecord, String> ROLE_KEY = createField(DSL.name("role_key"), SQLDataType.VARCHAR(32).nullable(false), this, "");

    /**
     * The column <code>system-db.system_role_creator.creator</code>.
     */
    public final TableField<SystemRoleCreatorRecord, String> CREATOR = createField(DSL.name("creator"), SQLDataType.VARCHAR(32).nullable(false), this, "");

    /**
     * The column <code>system-db.system_role_creator.alias</code>.
     */
    public final TableField<SystemRoleCreatorRecord, String> ALIAS = createField(DSL.name("alias"), SQLDataType.VARCHAR(32).nullable(false), this, "");

    /**
     * The column <code>system-db.system_role_creator.name</code>.
     */
    public final TableField<SystemRoleCreatorRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(32).nullable(false), this, "");

    /**
     * The column <code>system-db.system_role_creator.auto_create</code>.
     */
    public final TableField<SystemRoleCreatorRecord, Byte> AUTO_CREATE = createField(DSL.name("auto_create"), SQLDataType.TINYINT.nullable(false).defaultValue(DSL.field(DSL.raw("0"), SQLDataType.TINYINT)), this, "");

    private SystemRoleCreator(Name alias, Table<SystemRoleCreatorRecord> aliased) {
        this(alias, aliased, null);
    }

    private SystemRoleCreator(Name alias, Table<SystemRoleCreatorRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>system-db.system_role_creator</code> table
     * reference
     */
    public SystemRoleCreator(String alias) {
        this(DSL.name(alias), SYSTEM_ROLE_CREATOR);
    }

    /**
     * Create an aliased <code>system-db.system_role_creator</code> table
     * reference
     */
    public SystemRoleCreator(Name alias) {
        this(alias, SYSTEM_ROLE_CREATOR);
    }

    /**
     * Create a <code>system-db.system_role_creator</code> table reference
     */
    public SystemRoleCreator() {
        this(DSL.name("system_role_creator"), null);
    }

    public <O extends Record> SystemRoleCreator(Table<O> child, ForeignKey<O, SystemRoleCreatorRecord> key) {
        super(child, key, SYSTEM_ROLE_CREATOR);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : SystemDb.SYSTEM_DB;
    }

    @Override
    public UniqueKey<SystemRoleCreatorRecord> getPrimaryKey() {
        return Keys.KEY_SYSTEM_ROLE_CREATOR_PRIMARY;
    }

    @Override
    public SystemRoleCreator as(String alias) {
        return new SystemRoleCreator(DSL.name(alias), this);
    }

    @Override
    public SystemRoleCreator as(Name alias) {
        return new SystemRoleCreator(alias, this);
    }

    @Override
    public SystemRoleCreator as(Table<?> alias) {
        return new SystemRoleCreator(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public SystemRoleCreator rename(String name) {
        return new SystemRoleCreator(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public SystemRoleCreator rename(Name name) {
        return new SystemRoleCreator(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public SystemRoleCreator rename(Table<?> name) {
        return new SystemRoleCreator(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<String, String, String, String, Byte> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function5<? super String, ? super String, ? super String, ? super String, ? super Byte, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function5<? super String, ? super String, ? super String, ? super String, ? super Byte, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
