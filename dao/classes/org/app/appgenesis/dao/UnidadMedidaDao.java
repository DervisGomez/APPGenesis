package org.app.appgenesis.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import org.app.appgenesis.dao.UnidadMedida;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table UNIDAD_MEDIDA.
*/
public class UnidadMedidaDao extends AbstractDao<UnidadMedida, Long> {

    public static final String TABLENAME = "UNIDAD_MEDIDA";

    /**
     * Properties of entity UnidadMedida.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Consecutivo = new Property(1, String.class, "consecutivo", false, "CONSECUTIVO");
        public final static Property Valor = new Property(2, String.class, "valor", false, "VALOR");
        public final static Property Simbolo = new Property(3, String.class, "simbolo", false, "SIMBOLO");
    };


    public UnidadMedidaDao(DaoConfig config) {
        super(config);
    }
    
    public UnidadMedidaDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'UNIDAD_MEDIDA' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'CONSECUTIVO' TEXT," + // 1: consecutivo
                "'VALOR' TEXT," + // 2: valor
                "'SIMBOLO' TEXT);"); // 3: simbolo
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'UNIDAD_MEDIDA'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, UnidadMedida entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String consecutivo = entity.getConsecutivo();
        if (consecutivo != null) {
            stmt.bindString(2, consecutivo);
        }
 
        String valor = entity.getValor();
        if (valor != null) {
            stmt.bindString(3, valor);
        }
 
        String simbolo = entity.getSimbolo();
        if (simbolo != null) {
            stmt.bindString(4, simbolo);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public UnidadMedida readEntity(Cursor cursor, int offset) {
        UnidadMedida entity = new UnidadMedida( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // consecutivo
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // valor
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // simbolo
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, UnidadMedida entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setConsecutivo(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setValor(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSimbolo(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(UnidadMedida entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(UnidadMedida entity) {
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