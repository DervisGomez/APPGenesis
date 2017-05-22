package org.app.appgenesis.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import org.app.appgenesis.dao.GOP_ORDEESTA;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table GOP__ORDEESTA.
*/
public class GOP_ORDEESTADao extends AbstractDao<GOP_ORDEESTA, Long> {

    public static final String TABLENAME = "GOP__ORDEESTA";

    /**
     * Properties of entity GOP_ORDEESTA.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ORESCONS = new Property(1, String.class, "ORESCONS", false, "ORESCONS");
        public final static Property ORESORDE = new Property(2, String.class, "ORESORDE", false, "ORESORDE");
        public final static Property ORESESTA = new Property(3, String.class, "ORESESTA", false, "ORESESTA");
        public final static Property ORESOBSE = new Property(4, String.class, "ORESOBSE", false, "ORESOBSE");
        public final static Property ORESUSCR = new Property(5, String.class, "ORESUSCR", false, "ORESUSCR");
        public final static Property ORESFECR = new Property(6, String.class, "ORESFECR", false, "ORESFECR");
    };


    public GOP_ORDEESTADao(DaoConfig config) {
        super(config);
    }
    
    public GOP_ORDEESTADao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'GOP__ORDEESTA' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'ORESCONS' TEXT," + // 1: ORESCONS
                "'ORESORDE' TEXT," + // 2: ORESORDE
                "'ORESESTA' TEXT," + // 3: ORESESTA
                "'ORESOBSE' TEXT," + // 4: ORESOBSE
                "'ORESUSCR' TEXT," + // 5: ORESUSCR
                "'ORESFECR' TEXT);"); // 6: ORESFECR
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'GOP__ORDEESTA'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, GOP_ORDEESTA entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String ORESCONS = entity.getORESCONS();
        if (ORESCONS != null) {
            stmt.bindString(2, ORESCONS);
        }
 
        String ORESORDE = entity.getORESORDE();
        if (ORESORDE != null) {
            stmt.bindString(3, ORESORDE);
        }
 
        String ORESESTA = entity.getORESESTA();
        if (ORESESTA != null) {
            stmt.bindString(4, ORESESTA);
        }
 
        String ORESOBSE = entity.getORESOBSE();
        if (ORESOBSE != null) {
            stmt.bindString(5, ORESOBSE);
        }
 
        String ORESUSCR = entity.getORESUSCR();
        if (ORESUSCR != null) {
            stmt.bindString(6, ORESUSCR);
        }
 
        String ORESFECR = entity.getORESFECR();
        if (ORESFECR != null) {
            stmt.bindString(7, ORESFECR);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public GOP_ORDEESTA readEntity(Cursor cursor, int offset) {
        GOP_ORDEESTA entity = new GOP_ORDEESTA( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // ORESCONS
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // ORESORDE
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // ORESESTA
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // ORESOBSE
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // ORESUSCR
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // ORESFECR
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, GOP_ORDEESTA entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setORESCONS(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setORESORDE(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setORESESTA(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setORESOBSE(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setORESUSCR(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setORESFECR(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(GOP_ORDEESTA entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(GOP_ORDEESTA entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
