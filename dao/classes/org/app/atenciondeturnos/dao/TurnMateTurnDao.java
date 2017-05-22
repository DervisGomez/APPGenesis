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

import org.app.atenciondeturnos.dao.TurnMateTurn;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table TURN_MATE_TURN.
*/
public class TurnMateTurnDao extends AbstractDao<TurnMateTurn, Long> {

    public static final String TABLENAME = "TURN_MATE_TURN";

    /**
     * Properties of entity TurnMateTurn.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Cantidad = new Property(1, Integer.class, "cantidad", false, "CANTIDAD");
        public final static Property IdMaterial = new Property(2, Long.class, "idMaterial", false, "ID_MATERIAL");
        public final static Property IdTurno = new Property(3, Long.class, "idTurno", false, "ID_TURNO");
    };

    private Query<TurnMateTurn> turnMate_TurnMateQuery;
    private Query<TurnMateTurn> gDB_TURNOS_TurnMateQuery;

    public TurnMateTurnDao(DaoConfig config) {
        super(config);
    }
    
    public TurnMateTurnDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'TURN_MATE_TURN' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'CANTIDAD' INTEGER," + // 1: cantidad
                "'ID_MATERIAL' INTEGER," + // 2: idMaterial
                "'ID_TURNO' INTEGER);"); // 3: idTurno
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'TURN_MATE_TURN'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, TurnMateTurn entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer cantidad = entity.getCantidad();
        if (cantidad != null) {
            stmt.bindLong(2, cantidad);
        }
 
        Long idMaterial = entity.getIdMaterial();
        if (idMaterial != null) {
            stmt.bindLong(3, idMaterial);
        }
 
        Long idTurno = entity.getIdTurno();
        if (idTurno != null) {
            stmt.bindLong(4, idTurno);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public TurnMateTurn readEntity(Cursor cursor, int offset) {
        TurnMateTurn entity = new TurnMateTurn( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // cantidad
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // idMaterial
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3) // idTurno
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, TurnMateTurn entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCantidad(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setIdMaterial(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setIdTurno(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(TurnMateTurn entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(TurnMateTurn entity) {
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
    
    /** Internal query to resolve the "turnMate" to-many relationship of TurnMate. */
    public List<TurnMateTurn> _queryTurnMate_TurnMate(Long idMaterial) {
        synchronized (this) {
            if (turnMate_TurnMateQuery == null) {
                QueryBuilder<TurnMateTurn> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.IdMaterial.eq(null));
                turnMate_TurnMateQuery = queryBuilder.build();
            }
        }
        Query<TurnMateTurn> query = turnMate_TurnMateQuery.forCurrentThread();
        query.setParameter(0, idMaterial);
        return query.list();
    }

    /** Internal query to resolve the "turnMate" to-many relationship of GDB_TURNOS. */
    public List<TurnMateTurn> _queryGDB_TURNOS_TurnMate(Long idTurno) {
        synchronized (this) {
            if (gDB_TURNOS_TurnMateQuery == null) {
                QueryBuilder<TurnMateTurn> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.IdTurno.eq(null));
                gDB_TURNOS_TurnMateQuery = queryBuilder.build();
            }
        }
        Query<TurnMateTurn> query = gDB_TURNOS_TurnMateQuery.forCurrentThread();
        query.setParameter(0, idTurno);
        return query.list();
    }

}