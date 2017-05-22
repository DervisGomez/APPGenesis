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

import org.app.appgenesis.dao.Ordecost;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ORDECOST.
*/
public class OrdecostDao extends AbstractDao<Ordecost, Long> {

    public static final String TABLENAME = "ORDECOST";

    /**
     * Properties of entity Ordecost.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Usua = new Property(1, String.class, "usua", false, "USUA");
        public final static Property Sscr = new Property(2, String.class, "sscr", false, "SSCR");
        public final static Property Gene = new Property(3, Boolean.class, "gene", false, "GENE");
        public final static Property Valor = new Property(4, String.class, "valor", false, "VALOR");
        public final static Property Resp = new Property(5, String.class, "resp", false, "RESP");
        public final static Property IdOrden = new Property(6, Long.class, "IdOrden", false, "ID_ORDEN");
    };

    private Query<Ordecost> gro_orden_OrdecostQuery;

    public OrdecostDao(DaoConfig config) {
        super(config);
    }
    
    public OrdecostDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ORDECOST' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'USUA' TEXT," + // 1: usua
                "'SSCR' TEXT," + // 2: sscr
                "'GENE' INTEGER," + // 3: gene
                "'VALOR' TEXT," + // 4: valor
                "'RESP' TEXT," + // 5: resp
                "'ID_ORDEN' INTEGER);"); // 6: IdOrden
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ORDECOST'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Ordecost entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String usua = entity.getUsua();
        if (usua != null) {
            stmt.bindString(2, usua);
        }
 
        String sscr = entity.getSscr();
        if (sscr != null) {
            stmt.bindString(3, sscr);
        }
 
        Boolean gene = entity.getGene();
        if (gene != null) {
            stmt.bindLong(4, gene ? 1l: 0l);
        }
 
        String valor = entity.getValor();
        if (valor != null) {
            stmt.bindString(5, valor);
        }
 
        String resp = entity.getResp();
        if (resp != null) {
            stmt.bindString(6, resp);
        }
 
        Long IdOrden = entity.getIdOrden();
        if (IdOrden != null) {
            stmt.bindLong(7, IdOrden);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Ordecost readEntity(Cursor cursor, int offset) {
        Ordecost entity = new Ordecost( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // usua
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // sscr
            cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0, // gene
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // valor
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // resp
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6) // IdOrden
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Ordecost entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUsua(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSscr(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setGene(cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0);
        entity.setValor(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setResp(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setIdOrden(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Ordecost entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Ordecost entity) {
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
    
    /** Internal query to resolve the "ordecost" to-many relationship of Gro_orden. */
    public List<Ordecost> _queryGro_orden_Ordecost(Long IdOrden) {
        synchronized (this) {
            if (gro_orden_OrdecostQuery == null) {
                QueryBuilder<Ordecost> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.IdOrden.eq(null));
                gro_orden_OrdecostQuery = queryBuilder.build();
            }
        }
        Query<Ordecost> query = gro_orden_OrdecostQuery.forCurrentThread();
        query.setParameter(0, IdOrden);
        return query.list();
    }

}