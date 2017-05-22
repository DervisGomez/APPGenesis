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

import org.app.appgenesis.dao.Firma;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table FIRMA.
*/
public class FirmaDao extends AbstractDao<Firma, Long> {

    public static final String TABLENAME = "FIRMA";

    /**
     * Properties of entity Firma.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Documento = new Property(1, String.class, "documento", false, "DOCUMENTO");
        public final static Property Nombre = new Property(2, String.class, "nombre", false, "NOMBRE");
        public final static Property Correo = new Property(3, String.class, "Correo", false, "CORREO");
        public final static Property Fecha = new Property(4, String.class, "fecha", false, "FECHA");
        public final static Property Estado = new Property(5, String.class, "estado", false, "ESTADO");
        public final static Property Usuario = new Property(6, String.class, "usuario", false, "USUARIO");
        public final static Property Foto = new Property(7, String.class, "foto", false, "FOTO");
        public final static Property IdOrden = new Property(8, Long.class, "idOrden", false, "ID_ORDEN");
    };

    private Query<Firma> gro_orden_FirmaQuery;

    public FirmaDao(DaoConfig config) {
        super(config);
    }
    
    public FirmaDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'FIRMA' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'DOCUMENTO' TEXT," + // 1: documento
                "'NOMBRE' TEXT," + // 2: nombre
                "'CORREO' TEXT," + // 3: Correo
                "'FECHA' TEXT," + // 4: fecha
                "'ESTADO' TEXT," + // 5: estado
                "'USUARIO' TEXT," + // 6: usuario
                "'FOTO' TEXT," + // 7: foto
                "'ID_ORDEN' INTEGER);"); // 8: idOrden
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'FIRMA'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Firma entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String documento = entity.getDocumento();
        if (documento != null) {
            stmt.bindString(2, documento);
        }
 
        String nombre = entity.getNombre();
        if (nombre != null) {
            stmt.bindString(3, nombre);
        }
 
        String Correo = entity.getCorreo();
        if (Correo != null) {
            stmt.bindString(4, Correo);
        }
 
        String fecha = entity.getFecha();
        if (fecha != null) {
            stmt.bindString(5, fecha);
        }
 
        String estado = entity.getEstado();
        if (estado != null) {
            stmt.bindString(6, estado);
        }
 
        String usuario = entity.getUsuario();
        if (usuario != null) {
            stmt.bindString(7, usuario);
        }
 
        String foto = entity.getFoto();
        if (foto != null) {
            stmt.bindString(8, foto);
        }
 
        Long idOrden = entity.getIdOrden();
        if (idOrden != null) {
            stmt.bindLong(9, idOrden);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Firma readEntity(Cursor cursor, int offset) {
        Firma entity = new Firma( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // documento
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // nombre
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // Correo
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // fecha
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // estado
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // usuario
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // foto
            cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8) // idOrden
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Firma entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDocumento(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNombre(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCorreo(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setFecha(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setEstado(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setUsuario(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setFoto(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setIdOrden(cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Firma entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Firma entity) {
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
    
    /** Internal query to resolve the "firma" to-many relationship of Gro_orden. */
    public List<Firma> _queryGro_orden_Firma(Long idOrden) {
        synchronized (this) {
            if (gro_orden_FirmaQuery == null) {
                QueryBuilder<Firma> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.IdOrden.eq(null));
                gro_orden_FirmaQuery = queryBuilder.build();
            }
        }
        Query<Firma> query = gro_orden_FirmaQuery.forCurrentThread();
        query.setParameter(0, idOrden);
        return query.list();
    }

}