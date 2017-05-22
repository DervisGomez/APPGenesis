package org.app.atenciondeturnos.dao;

import java.util.List;
import org.app.atenciondeturnos.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table TURN_PERS.
 */
public class TurnPers {

    private Long id;
    private Integer perscons;
    private Integer persaror;
    private Integer persopid;
    private Integer perstipo;
    private String persnomb;
    private Integer perstiid;
    private String persiden;
    private String persusop;
    private String persusua;
    private String persesta;
    private String persuscr;
    private String persfecr;
    private String unoppers;
    private String peuopers;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient TurnPersDao myDao;

    private List<TurnPersTurn> turnPers;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public TurnPers() {
    }

    public TurnPers(Long id) {
        this.id = id;
    }

    public TurnPers(Long id, Integer perscons, Integer persaror, Integer persopid, Integer perstipo, String persnomb, Integer perstiid, String persiden, String persusop, String persusua, String persesta, String persuscr, String persfecr, String unoppers, String peuopers) {
        this.id = id;
        this.perscons = perscons;
        this.persaror = persaror;
        this.persopid = persopid;
        this.perstipo = perstipo;
        this.persnomb = persnomb;
        this.perstiid = perstiid;
        this.persiden = persiden;
        this.persusop = persusop;
        this.persusua = persusua;
        this.persesta = persesta;
        this.persuscr = persuscr;
        this.persfecr = persfecr;
        this.unoppers = unoppers;
        this.peuopers = peuopers;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTurnPersDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPerscons() {
        return perscons;
    }

    public void setPerscons(Integer perscons) {
        this.perscons = perscons;
    }

    public Integer getPersaror() {
        return persaror;
    }

    public void setPersaror(Integer persaror) {
        this.persaror = persaror;
    }

    public Integer getPersopid() {
        return persopid;
    }

    public void setPersopid(Integer persopid) {
        this.persopid = persopid;
    }

    public Integer getPerstipo() {
        return perstipo;
    }

    public void setPerstipo(Integer perstipo) {
        this.perstipo = perstipo;
    }

    public String getPersnomb() {
        return persnomb;
    }

    public void setPersnomb(String persnomb) {
        this.persnomb = persnomb;
    }

    public Integer getPerstiid() {
        return perstiid;
    }

    public void setPerstiid(Integer perstiid) {
        this.perstiid = perstiid;
    }

    public String getPersiden() {
        return persiden;
    }

    public void setPersiden(String persiden) {
        this.persiden = persiden;
    }

    public String getPersusop() {
        return persusop;
    }

    public void setPersusop(String persusop) {
        this.persusop = persusop;
    }

    public String getPersusua() {
        return persusua;
    }

    public void setPersusua(String persusua) {
        this.persusua = persusua;
    }

    public String getPersesta() {
        return persesta;
    }

    public void setPersesta(String persesta) {
        this.persesta = persesta;
    }

    public String getPersuscr() {
        return persuscr;
    }

    public void setPersuscr(String persuscr) {
        this.persuscr = persuscr;
    }

    public String getPersfecr() {
        return persfecr;
    }

    public void setPersfecr(String persfecr) {
        this.persfecr = persfecr;
    }

    public String getUnoppers() {
        return unoppers;
    }

    public void setUnoppers(String unoppers) {
        this.unoppers = unoppers;
    }

    public String getPeuopers() {
        return peuopers;
    }

    public void setPeuopers(String peuopers) {
        this.peuopers = peuopers;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<TurnPersTurn> getTurnPers() {
        if (turnPers == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TurnPersTurnDao targetDao = daoSession.getTurnPersTurnDao();
            List<TurnPersTurn> turnPersNew = targetDao._queryTurnPers_TurnPers(id);
            synchronized (this) {
                if(turnPers == null) {
                    turnPers = turnPersNew;
                }
            }
        }
        return turnPers;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetTurnPers() {
        turnPers = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}