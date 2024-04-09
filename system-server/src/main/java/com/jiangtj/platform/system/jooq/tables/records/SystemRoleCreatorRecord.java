/*
 * This file is generated by jOOQ.
 */
package com.jiangtj.platform.system.jooq.tables.records;


import com.jiangtj.platform.system.jooq.tables.SystemRoleCreator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SystemRoleCreatorRecord extends UpdatableRecordImpl<SystemRoleCreatorRecord> implements Record5<String, String, String, String, Byte> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>system-db.system_role_creator.role_key</code>.
     */
    public void setRoleKey(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>system-db.system_role_creator.role_key</code>.
     */
    @NotNull
    @Size(max = 32)
    public String getRoleKey() {
        return (String) get(0);
    }

    /**
     * Setter for <code>system-db.system_role_creator.creator</code>.
     */
    public void setCreator(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>system-db.system_role_creator.creator</code>.
     */
    @NotNull
    @Size(max = 32)
    public String getCreator() {
        return (String) get(1);
    }

    /**
     * Setter for <code>system-db.system_role_creator.alias</code>.
     */
    public void setAlias(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>system-db.system_role_creator.alias</code>.
     */
    @NotNull
    @Size(max = 32)
    public String getAlias() {
        return (String) get(2);
    }

    /**
     * Setter for <code>system-db.system_role_creator.name</code>.
     */
    public void setName(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>system-db.system_role_creator.name</code>.
     */
    @NotNull
    @Size(max = 32)
    public String getName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>system-db.system_role_creator.auto_create</code>.
     */
    public void setAutoCreate(Byte value) {
        set(4, value);
    }

    /**
     * Getter for <code>system-db.system_role_creator.auto_create</code>.
     */
    public Byte getAutoCreate() {
        return (Byte) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<String, String> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<String, String, String, String, Byte> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<String, String, String, String, Byte> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return SystemRoleCreator.SYSTEM_ROLE_CREATOR.ROLE_KEY;
    }

    @Override
    public Field<String> field2() {
        return SystemRoleCreator.SYSTEM_ROLE_CREATOR.CREATOR;
    }

    @Override
    public Field<String> field3() {
        return SystemRoleCreator.SYSTEM_ROLE_CREATOR.ALIAS;
    }

    @Override
    public Field<String> field4() {
        return SystemRoleCreator.SYSTEM_ROLE_CREATOR.NAME;
    }

    @Override
    public Field<Byte> field5() {
        return SystemRoleCreator.SYSTEM_ROLE_CREATOR.AUTO_CREATE;
    }

    @Override
    public String component1() {
        return getRoleKey();
    }

    @Override
    public String component2() {
        return getCreator();
    }

    @Override
    public String component3() {
        return getAlias();
    }

    @Override
    public String component4() {
        return getName();
    }

    @Override
    public Byte component5() {
        return getAutoCreate();
    }

    @Override
    public String value1() {
        return getRoleKey();
    }

    @Override
    public String value2() {
        return getCreator();
    }

    @Override
    public String value3() {
        return getAlias();
    }

    @Override
    public String value4() {
        return getName();
    }

    @Override
    public Byte value5() {
        return getAutoCreate();
    }

    @Override
    public SystemRoleCreatorRecord value1(String value) {
        setRoleKey(value);
        return this;
    }

    @Override
    public SystemRoleCreatorRecord value2(String value) {
        setCreator(value);
        return this;
    }

    @Override
    public SystemRoleCreatorRecord value3(String value) {
        setAlias(value);
        return this;
    }

    @Override
    public SystemRoleCreatorRecord value4(String value) {
        setName(value);
        return this;
    }

    @Override
    public SystemRoleCreatorRecord value5(Byte value) {
        setAutoCreate(value);
        return this;
    }

    @Override
    public SystemRoleCreatorRecord values(String value1, String value2, String value3, String value4, Byte value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached SystemRoleCreatorRecord
     */
    public SystemRoleCreatorRecord() {
        super(SystemRoleCreator.SYSTEM_ROLE_CREATOR);
    }

    /**
     * Create a detached, initialised SystemRoleCreatorRecord
     */
    public SystemRoleCreatorRecord(String roleKey, String creator, String alias, String name, Byte autoCreate) {
        super(SystemRoleCreator.SYSTEM_ROLE_CREATOR);

        setRoleKey(roleKey);
        setCreator(creator);
        setAlias(alias);
        setName(name);
        setAutoCreate(autoCreate);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised SystemRoleCreatorRecord
     */
    public SystemRoleCreatorRecord(com.jiangtj.platform.system.jooq.tables.pojos.SystemRoleCreator value) {
        super(SystemRoleCreator.SYSTEM_ROLE_CREATOR);

        if (value != null) {
            setRoleKey(value.roleKey());
            setCreator(value.creator());
            setAlias(value.alias());
            setName(value.name());
            setAutoCreate(value.autoCreate());
            resetChangedOnNotNull();
        }
    }
}
