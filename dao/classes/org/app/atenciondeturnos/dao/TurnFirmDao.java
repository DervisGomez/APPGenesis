package org.app.atenciondeturnos.dao;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import org.app.atenciondeturnos.dao.TurnFirm;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table TURN_FIRM.
*/
public class TurnFirmDao extends AbstractDao<TurnFirm, Long> {

    public static final String TABLENAME = "TURN_FIRM";

    /**
     * Properties of entity TurnFirm.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Codigo = new Property(1, String.class, "codigo", false, "CODIGO");
        public final static Property Nombre = new Property(2, String.class, "nombre", false, "NOMBRE");
        public final static Property Correo = new Property(3, String.class, "Correo", false, "CORREO");
        public final static Property Fecha = new Property(4, String.class, "fecha", false, "FECHA");
        public final static Property Estado = new Property(5, String.class, "estado", false, "ESTADO");
        public final static Property Usuario = new Property(6, String.class, "usuario", false, "USUARIO");
        public final static Property Foto = new Property(7, String.class, "foto", false, "FOTO");
        public final static Property IdTurno = new Property(8, Long.class, "idTurno", false, "ID_TURNO");
    };

    private Query<TurnFirm> gDB_TURNOS_FirmaQuery;

    public TurnFirmDao(DaoConfig config) {
        super(config);
    }
    
    public TurnFirmDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'TURN_FIRM' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'CODIGO' TEXT," + // 1: codigo
                "'NOMBRE' TEXT," + // 2: nombre
                "'CORREO' TEXT," + // 3: Correo
                "'FECHA' TEXT," + // 4: fecha
                "'ESTADO' TEXT," + // 5: estado
                "'USUARIO' TEXT," + // 6: usuario
                "'FOTO' TEXT," + // 7: foto
                "'ID_TURNO' INTEGER);"); // 8: idTurno
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'TURN_FIRM'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, TurnFirm entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String codigo = entity.getCodigo();
        if (codigo != null) {
            stmt.bindString(2, codigo);
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
 
        Long idTurno = entity.getIdTurno();
        if (idTurno != null) {
            stmt.bindLong(9, idTurno);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public TurnFirm readEntity(Cursor cursor, int offset) {
        TurnFirm entity = new TurnFirm( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // codigo
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // nombre
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // Correo
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // fecha
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // estado
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // usuario
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // foto
            cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8) // idTurno
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, TurnFirm entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCodigo(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNombre(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCorreo(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setFecha(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setEstado(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setUsuario(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setFoto(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setIdTurno(cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(TurnFirm entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(TurnFirm entity) {
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
    
    /** Internal query to resolve the "firma" to-many relationship of GDB_TURNOS. */
    public List<TurnFirm> _queryGDB_TURNOS_Firma(Long idTurno) {
        synchronized (this) {
            if (gDB_TURNOS_FirmaQuery == null) {
                QueryBuilder<TurnFirm> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.IdTurno.eq(null));
                gDB_TURNOS_FirmaQuery = queryBuilder.build();
            }
        }
        Query<TurnFirm> query = gDB_TURNOS_FirmaQuery.forCurrentThread();
        query.setParameter(0, idTurno);
        return query.list();
    }

}