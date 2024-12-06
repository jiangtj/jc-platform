/*
 * This file is generated by jOOQ.
 */
package com.jiangtj.platform.system.jooq.tables;


import com.jiangtj.platform.system.jooq.JcAdmin;
import com.jiangtj.platform.system.jooq.Keys;
import com.jiangtj.platform.system.jooq.tables.records.SystemUserRecord;

import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function4;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row4;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class SystemUser extends TableImpl<SystemUserRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>jc_admin.system_user</code>
     */
    public static final SystemUser SYSTEM_USER = new SystemUser();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SystemUserRecord> getRecordType() {
        return SystemUserRecord.class;
    }

    /**
     * The column <code>jc_admin.system_user.id</code>.
     */
    public final TableField<SystemUserRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>jc_admin.system_user.username</code>.
     */
    public final TableField<SystemUserRecord, String> USERNAME = createField(DSL.name("username"), SQLDataType.VARCHAR(32).nullable(false), this, "");

    /**
     * The column <code>jc_admin.system_user.password</code>.
     */
    public final TableField<SystemUserRecord, String> PASSWORD = createField(DSL.name("password"), SQLDataType.VARCHAR(32).nullable(false), this, "");

    /**
     * The column <code>jc_admin.system_user.is_deleted</code>.
     */
    public final TableField<SystemUserRecord, Byte> IS_DELETED = createField(DSL.name("is_deleted"), SQLDataType.TINYINT.nullable(false).defaultValue(DSL.inline("0", SQLDataType.TINYINT)), this, "");

    private SystemUser(Name alias, Table<SystemUserRecord> aliased) {
        this(alias, aliased, null);
    }

    private SystemUser(Name alias, Table<SystemUserRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>jc_admin.system_user</code> table reference
     */
    public SystemUser(String alias) {
        this(DSL.name(alias), SYSTEM_USER);
    }

    /**
     * Create an aliased <code>jc_admin.system_user</code> table reference
     */
    public SystemUser(Name alias) {
        this(alias, SYSTEM_USER);
    }

    /**
     * Create a <code>jc_admin.system_user</code> table reference
     */
    public SystemUser() {
        this(DSL.name("system_user"), null);
    }

    public <O extends Record> SystemUser(Table<O> child, ForeignKey<O, SystemUserRecord> key) {
        super(child, key, SYSTEM_USER);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : JcAdmin.JC_ADMIN;
    }

    @Override
    public Identity<SystemUserRecord, Long> getIdentity() {
        return (Identity<SystemUserRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<SystemUserRecord> getPrimaryKey() {
        return Keys.KEY_SYSTEM_USER_PRIMARY;
    }

    @Override
    public SystemUser as(String alias) {
        return new SystemUser(DSL.name(alias), this);
    }

    @Override
    public SystemUser as(Name alias) {
        return new SystemUser(alias, this);
    }

    @Override
    public SystemUser as(Table<?> alias) {
        return new SystemUser(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public SystemUser rename(String name) {
        return new SystemUser(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public SystemUser rename(Name name) {
        return new SystemUser(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public SystemUser rename(Table<?> name) {
        return new SystemUser(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<Long, String, String, Byte> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function4<? super Long, ? super String, ? super String, ? super Byte, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function4<? super Long, ? super String, ? super String, ? super Byte, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
