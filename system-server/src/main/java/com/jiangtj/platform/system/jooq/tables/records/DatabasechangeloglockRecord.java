/*
 * This file is generated by jOOQ.
 */
package com.jiangtj.platform.system.jooq.tables.records;


import com.jiangtj.platform.system.jooq.tables.Databasechangeloglock;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class DatabasechangeloglockRecord extends UpdatableRecordImpl<DatabasechangeloglockRecord> implements Record4<Integer, Byte, LocalDateTime, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>jc_admin.DATABASECHANGELOGLOCK.ID</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>jc_admin.DATABASECHANGELOGLOCK.ID</code>.
     */
    @NotNull
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>jc_admin.DATABASECHANGELOGLOCK.LOCKED</code>.
     */
    public void setLocked(Byte value) {
        set(1, value);
    }

    /**
     * Getter for <code>jc_admin.DATABASECHANGELOGLOCK.LOCKED</code>.
     */
    @NotNull
    public Byte getLocked() {
        return (Byte) get(1);
    }

    /**
     * Setter for <code>jc_admin.DATABASECHANGELOGLOCK.LOCKGRANTED</code>.
     */
    public void setLockgranted(LocalDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>jc_admin.DATABASECHANGELOGLOCK.LOCKGRANTED</code>.
     */
    public LocalDateTime getLockgranted() {
        return (LocalDateTime) get(2);
    }

    /**
     * Setter for <code>jc_admin.DATABASECHANGELOGLOCK.LOCKEDBY</code>.
     */
    public void setLockedby(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>jc_admin.DATABASECHANGELOGLOCK.LOCKEDBY</code>.
     */
    @Size(max = 255)
    public String getLockedby() {
        return (String) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<Integer, Byte, LocalDateTime, String> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<Integer, Byte, LocalDateTime, String> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Databasechangeloglock.DATABASECHANGELOGLOCK.ID;
    }

    @Override
    public Field<Byte> field2() {
        return Databasechangeloglock.DATABASECHANGELOGLOCK.LOCKED;
    }

    @Override
    public Field<LocalDateTime> field3() {
        return Databasechangeloglock.DATABASECHANGELOGLOCK.LOCKGRANTED;
    }

    @Override
    public Field<String> field4() {
        return Databasechangeloglock.DATABASECHANGELOGLOCK.LOCKEDBY;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public Byte component2() {
        return getLocked();
    }

    @Override
    public LocalDateTime component3() {
        return getLockgranted();
    }

    @Override
    public String component4() {
        return getLockedby();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public Byte value2() {
        return getLocked();
    }

    @Override
    public LocalDateTime value3() {
        return getLockgranted();
    }

    @Override
    public String value4() {
        return getLockedby();
    }

    @Override
    public DatabasechangeloglockRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public DatabasechangeloglockRecord value2(Byte value) {
        setLocked(value);
        return this;
    }

    @Override
    public DatabasechangeloglockRecord value3(LocalDateTime value) {
        setLockgranted(value);
        return this;
    }

    @Override
    public DatabasechangeloglockRecord value4(String value) {
        setLockedby(value);
        return this;
    }

    @Override
    public DatabasechangeloglockRecord values(Integer value1, Byte value2, LocalDateTime value3, String value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached DatabasechangeloglockRecord
     */
    public DatabasechangeloglockRecord() {
        super(Databasechangeloglock.DATABASECHANGELOGLOCK);
    }

    /**
     * Create a detached, initialised DatabasechangeloglockRecord
     */
    public DatabasechangeloglockRecord(Integer id, Byte locked, LocalDateTime lockgranted, String lockedby) {
        super(Databasechangeloglock.DATABASECHANGELOGLOCK);

        setId(id);
        setLocked(locked);
        setLockgranted(lockgranted);
        setLockedby(lockedby);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised DatabasechangeloglockRecord
     */
    public DatabasechangeloglockRecord(com.jiangtj.platform.system.jooq.tables.pojos.Databasechangeloglock value) {
        super(Databasechangeloglock.DATABASECHANGELOGLOCK);

        if (value != null) {
            setId(value.id());
            setLocked(value.locked());
            setLockgranted(value.lockgranted());
            setLockedby(value.lockedby());
            resetChangedOnNotNull();
        }
    }
}
