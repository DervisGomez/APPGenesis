package org.app.atenciondeturnos.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import org.app.atenciondeturnos.dao.GDB_VALVULA;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table GDB__VALVULA.
*/
public class GDB_VALVULADao extends AbstractDao<GDB_VALVULA, Long> {

    public static final String TABLENAME = "GDB__VALVULA";

    /**
     * Properties of entity GDB_VALVULA.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property VALVCONS = new Property(1, Long.class, "VALVCONS", false, "VALVCONS");
        public final static Property VALVDESC = new Property(2, String.class, "VALVDESC", false, "VALVDESC");
        public final static Property VALVDIRE = new Property(3, String.class, "VALVDIRE", false, "VALVDIRE");
        public final static Property VALVDIAM = new Property(4, String.class, "VALVDIAM", false, "VALVDIAM");
        public final static Property VALVCOOR = new Property(5, String.class, "VALVCOOR", false, "VALVCOOR");
        public final static Property VALVESTA = new Property(6, String.class, "VALVESTA", false, "VALVESTA");
        public final static Property VALVUSCR = new Property(7, String.class, "VALVUSCR", false, "VALVUSCR");
        public final static Property VALVFECR = new Property(8, String.class, "VALVFECR", false, "VALVFECR");
    };


    public GDB_VALVULADao(DaoConfig config) {
        super(config);
    }
    
    public GDB_VALVULADao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'GDB__VALVULA' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'VALVCONS' INTEGER," + // 1: VALVCONS
                "'VALVDESC' TEXT," + // 2: VALVDESC
                "'VALVDIRE' TEXT," + // 3: VALVDIRE
                "'VALVDIAM' TEXT," + // 4: VALVDIAM
                "'VALVCOOR' TEXT," + // 5: VALVCOOR
                "'VALVESTA' TEXT," + // 6: VALVESTA
                "'VALVUSCR' TEXT," + // 7: VALVUSCR
                "'VALVFECR' TEXT);"); // 8: VALVFECR
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'GDB__VALVULA'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, GDB_VALVULA entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long VALVCONS = entity.getVALVCONS();
        if (VALVCONS != null) {
            stmt.bindLong(2, VALVCONS);
        }
 
        String VALVDESC = entity.getVALVDESC();
        if (VALVDESC != null) {
            stmt.bindString(3, VALVDESC);
        }
 
        String VALVDIRE = entity.getVALVDIRE();
        if (VALVDIRE != null) {
            stmt.bindString(4, VALVDIRE);
        }
 
        String VALVDIAM = entity.getVALVDIAM();
        if (VALVDIAM != null) {
            stmt.bindString(5, VALVDIAM);
        }
 
        String VALVCOOR = entity.getVALVCOOR();
        if (VALVCOOR != null) {
            stmt.bindString(6, VALVCOOR);
        }
 
        String VALVESTA = entity.getVALVESTA();
        if (VALVESTA != null) {
            stmt.bindString(7, VALVESTA);
        }
 
        String VALVUSCR = entity.getVALVUSCR();
        if (VALVUSCR != null) {
            stmt.bindString(8, VALVUSCR);
        }
 
        String VALVFECR = entity.getVALVFECR();
        if (VALVFECR != null) {
            stmt.bindString(9, VALVFECR);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public GDB_VALVULA readEntity(Cursor cursor, int offset) {
        GDB_VALVULA entity = new GDB_VALVULA( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // VALVCONS
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // VALVDESC
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // VALVDIRE
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // VALVDIAM
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // VALVCOOR
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // VALVESTA
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // VALVUSCR
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // VALVFECR
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, GDB_VALVULA entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setVALVCONS(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setVALVDESC(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setVALVDIRE(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setVALVDIAM(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setVALVCOOR(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setVALVESTA(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setVALVUSCR(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setVALVFECR(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(GDB_VALVULA entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(GDB_VALVULA entity) {
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
