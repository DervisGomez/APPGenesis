package org.app.appgenesis.dao;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import org.app.appgenesis.dao.OrdeMate;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ORDE_MATE.
*/
public class OrdeMateDao extends AbstractDao<OrdeMate, Long> {

    public static final String TABLENAME = "ORDE_MATE";

    /**
     * Properties of entity OrdeMate.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Cantidad = new Property(1, String.class, "cantidad", false, "CANTIDAD");
        public final static Property IdMaterial = new Property(2, Long.class, "idMaterial", false, "ID_MATERIAL");
        public final static Property IdOrden = new Property(3, Long.class, "idOrden", false, "ID_ORDEN");
    };

    private Query<OrdeMate> material_OrdeMateQuery;
    private Query<OrdeMate> gro_orden_OrdeMateQuery;

    public OrdeMateDao(DaoConfig config) {
        super(config);
    }
    
    public OrdeMateDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ORDE_MATE' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'CANTIDAD' TEXT," + // 1: cantidad
                "'ID_MATERIAL' INTEGER," + // 2: idMaterial
                "'ID_ORDEN' INTEGER);"); // 3: idOrden
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ORDE_MATE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, OrdeMate entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String cantidad = entity.getCantidad();
        if (cantidad != null) {
            stmt.bindString(2, cantidad);
        }
 
        Long idMaterial = entity.getIdMaterial();
        if (idMaterial != null) {
            stmt.bindLong(3, idMaterial);
        }
 
        Long idOrden = entity.getIdOrden();
        if (idOrden != null) {
            stmt.bindLong(4, idOrden);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public OrdeMate readEntity(Cursor cursor, int offset) {
        OrdeMate entity = new OrdeMate( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // cantidad
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // idMaterial
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3) // idOrden
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, OrdeMate entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCantidad(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setIdMaterial(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setIdOrden(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(OrdeMate entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(OrdeMate entity) {
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
    
    /** Internal query to resolve the "ordeMate" to-many relationship of Material. */
    public List<OrdeMate> _queryMaterial_OrdeMate(Long idMaterial) {
        synchronized (this) {
            if (material_OrdeMateQuery == null) {
                QueryBuilder<OrdeMate> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.IdMaterial.eq(null));
                material_OrdeMateQuery = queryBuilder.build();
            }
        }
        Query<OrdeMate> query = material_OrdeMateQuery.forCurrentThread();
        query.setParameter(0, idMaterial);
        return query.list();
    }

    /** Internal query to resolve the "ordeMate" to-many relationship of Gro_orden. */
    public List<OrdeMate> _queryGro_orden_OrdeMate(Long idOrden) {
        synchronized (this) {
            if (gro_orden_OrdeMateQuery == null) {
                QueryBuilder<OrdeMate> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.IdOrden.eq(null));
                gro_orden_OrdeMateQuery = queryBuilder.build();
            }
        }
        Query<OrdeMate> query = gro_orden_OrdeMateQuery.forCurrentThread();
        query.setParameter(0, idOrden);
        return query.list();
    }

}
