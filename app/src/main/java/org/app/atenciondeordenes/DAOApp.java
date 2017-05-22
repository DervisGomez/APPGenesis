package org.app.atenciondeordenes;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import org.app.appgenesis.dao.CausalDao;
import org.app.appgenesis.dao.ComentarioDao;
import org.app.appgenesis.dao.DaoMaster;
import org.app.appgenesis.dao.DaoSession;
import org.app.appgenesis.dao.DibujoDao;
import org.app.appgenesis.dao.FirmaDao;
import org.app.appgenesis.dao.FotografiaDao;
import org.app.appgenesis.dao.GMA_CAUSALDao;
import org.app.appgenesis.dao.GMA_PKID;
import org.app.appgenesis.dao.GMA_PKIDDao;
import org.app.appgenesis.dao.GOP_ATRIBUTOSDao;
import org.app.appgenesis.dao.GOP_ORDEESTA;
import org.app.appgenesis.dao.GOP_ORDEESTADao;
import org.app.atenciondeturnos.dao.GDB_TURNOSDao;
import org.app.atenciondeturnos.dao.GDB_TURNPERSDao;
import org.app.atenciondeturnos.dao.GDB_VALVULADao;
import org.app.appgenesis.dao.Gma_costtitrDao;
import org.app.appgenesis.dao.Gop_ordeatriDao;
import org.app.appgenesis.dao.Gro_ordenDao;
import org.app.appgenesis.dao.MaterialDao;
import org.app.appgenesis.dao.OrdeMateDao;
import org.app.appgenesis.dao.OrdePersDao;
import org.app.appgenesis.dao.OrdecostDao;
import org.app.appgenesis.dao.PersonaDao;
import org.app.appgenesis.dao.UnidadMedidaDao;
import org.app.atenciondeturnos.dao.GMA_PKIDTurnoDao;
import org.app.atenciondeturnos.dao.TurnCome;
import org.app.atenciondeturnos.dao.TurnComeDao;
import org.app.atenciondeturnos.dao.TurnFirmDao;
import org.app.atenciondeturnos.dao.TurnMateDao;
import org.app.atenciondeturnos.dao.TurnMateTurnDao;
import org.app.atenciondeturnos.dao.TurnPers;
import org.app.atenciondeturnos.dao.TurnPersDao;
import org.app.atenciondeturnos.dao.TurnPersTurnDao;
import org.app.atenciondeturnos.dao.TurnUnidMediDao;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.AbstractDao;

/**
 * Created by dervis on 08/12/16.
 */
public class DAOApp extends Application {

    static PersonaDao personaDao;
    static Gro_ordenDao ordenDao;
    static MaterialDao materialDao;
    static FirmaDao firmaDao;
    static DibujoDao dibujoDao;
    static ComentarioDao comentarioDao;
    static OrdePersDao ordePersDao;
    static OrdeMateDao ordeMateDao;
    static UnidadMedidaDao unidadMedidaDao;
    static GMA_CAUSALDao causalDao;
    static Gop_ordeatriDao gopOrdeatriDao;
    static FotografiaDao fotografiaDao;
    static Gma_costtitrDao gmaCosttitrDao;
    static OrdecostDao ordecostDao;
    static GMA_PKIDDao gma_pkidDao;
    static GOP_ORDEESTADao gop_ordeesta;
    static GOP_ATRIBUTOSDao gop_atributosDao;
    static List<String> databaseName = new ArrayList<>();
    static List<String> entities = new ArrayList<>();

    static GDB_TURNOSDao gdb_turnosDao;
    static GDB_TURNPERSDao gdb_turnpersDao;
    static GDB_VALVULADao gdb_valvulaDao;
    static TurnComeDao turnComeDao;
    static TurnPersDao turnPersDao;
    static TurnPersTurnDao turnPersTurnDao;
    static TurnMateTurnDao turnMateTurnDao;
    static TurnMateDao turnMateDao;
    static TurnUnidMediDao turnUnidMediDao;
    static TurnFirmDao turnFirmDao;
    static GMA_PKIDTurnoDao gma_pkidTurnoDao;

    public static GDB_TURNOSDao getGdb_turnosDao(){
        return gdb_turnosDao;
    }
    public static GDB_TURNPERSDao getGdb_turnpersDao(){
        return gdb_turnpersDao;
    }
    public static GDB_VALVULADao getGdb_valvulaDao() {
        return gdb_valvulaDao;
    }
    public static TurnComeDao getTurnComeDao(){ return turnComeDao; }
    public static TurnPersDao getTurnPersDao(){ return turnPersDao; }
    public static TurnPersTurnDao getTurnPersTurnDao(){ return turnPersTurnDao; }
    public static TurnMateDao getTurnMateDao(){ return turnMateDao; }
    public static TurnMateTurnDao getTurnMateTurnDao(){ return turnMateTurnDao; }
    public static TurnUnidMediDao getTurnUnidMediDao(){ return turnUnidMediDao; }
    public static TurnFirmDao getTurnFirmDao(){ return turnFirmDao; }
    public static GMA_PKIDTurnoDao getGma_pkidTurnoDao(){ return gma_pkidTurnoDao; }

    public static PersonaDao getPersonaDao() {
        return personaDao;
    }

    public static Gro_ordenDao getGro_ordenDao(){
        return ordenDao;
    }

    public static MaterialDao getMaterialDao(){
        return materialDao;
    }

    public static FirmaDao getFirmaDao(){
        return firmaDao;
    }

    public static DibujoDao getDibujoDao(){
        return dibujoDao;
    }

    public static ComentarioDao getComentarioDao(){
        return comentarioDao;
    }

    public static OrdePersDao getOrdePersDao(){
        return ordePersDao;
    }

    public static OrdeMateDao getOrdeMateDao(){
        return ordeMateDao;
    }

    public static UnidadMedidaDao getUnidadMedidaDao() {
        return unidadMedidaDao;
    }

    public static GMA_CAUSALDao getGMA_CAUSALDao(){
        return causalDao;
    }

    public static Gop_ordeatriDao getGopOrdeatriDao(){
        return gopOrdeatriDao;
    }

    public static FotografiaDao getFotografiaDao(){
        return fotografiaDao;
    }

    public static Gma_costtitrDao getGmaCosttitrDao(){
        return gmaCosttitrDao;
    }

    public static OrdecostDao getOrdecostDao() {return ordecostDao;}
    public static GMA_PKIDDao getGma_pkidDao(){ return gma_pkidDao; }
    public static GOP_ORDEESTADao getGop_ordeestaDao(){ return gop_ordeesta; }
    public static GOP_ATRIBUTOSDao getGop_atributosDao(){ return gop_atributosDao;}

    public static List<String> getDatabaseName(){
        return databaseName;
    }

    public static List<String> getEntities(){
        return entities;
    }

    public static AbstractDao getEntityByName(String name){
        switch (name){

            case "PERSONA":
                return personaDao;
            case "GRO_ORDEN":
                return ordenDao;
            case "MATERIAL":
                return materialDao;
            case "FIRMA":
                return firmaDao;
            case "DIBUJO":
                return dibujoDao;
            case "COMENTARIO":
                return comentarioDao;
            case "ORDE_PERS":
                return ordePersDao;
            case "ORDE_MATE":
                return ordeMateDao;
            case "FOTOGRAFIA":
                return fotografiaDao;
            case "GOP_ORDEATRI":
                return gopOrdeatriDao;
            case "GMA_COSTTITR":
                return gmaCosttitrDao;
            default:
                return null;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "atenciondedanos", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        personaDao = daoSession.getPersonaDao();
        ordenDao = daoSession.getGro_ordenDao();
        materialDao = daoSession.getMaterialDao();
        firmaDao = daoSession.getFirmaDao();
        dibujoDao = daoSession.getDibujoDao();
        comentarioDao = daoSession.getComentarioDao();
        ordePersDao = daoSession.getOrdePersDao();
        ordeMateDao=daoSession.getOrdeMateDao();
        unidadMedidaDao=daoSession.getUnidadMedidaDao();
        causalDao=daoSession.getGMA_CAUSALDao();
        gopOrdeatriDao = daoSession.getGop_ordeatriDao();
        fotografiaDao = daoSession.getFotografiaDao();
        gmaCosttitrDao=daoSession.getGma_costtitrDao();
        ordecostDao=daoSession.getOrdecostDao();
        gma_pkidDao=daoSession.getGMA_PKIDDao();
        gop_ordeesta=daoSession.getGOP_ORDEESTADao();
        gop_atributosDao=daoSession.getGOP_ATRIBUTOSDao();

        org.app.atenciondeturnos.dao.DaoMaster.DevOpenHelper helper1 = new org.app.atenciondeturnos.dao.DaoMaster.DevOpenHelper(this, "atenciondeturnos", null);
        SQLiteDatabase db1 = helper1.getWritableDatabase();
        org.app.atenciondeturnos.dao.DaoMaster daoMaster1 = new org.app.atenciondeturnos.dao.DaoMaster(db1);
        org.app.atenciondeturnos.dao.DaoSession daoSession1=daoMaster1.newSession();

        gdb_turnosDao=daoSession1.getGDB_TURNOSDao();
        gdb_turnpersDao=daoSession1.getGDB_TURNPERSDao();
        gdb_valvulaDao=daoSession1.getGDB_VALVULADao();
        turnComeDao=daoSession1.getTurnComeDao();
        turnPersDao=daoSession1.getTurnPersDao();
        turnPersTurnDao=daoSession1.getTurnPersTurnDao();
        turnMateDao=daoSession1.getTurnMateDao();
        turnMateTurnDao=daoSession1.getTurnMateTurnDao();
        turnUnidMediDao=daoSession1.getTurnUnidMediDao();
        turnFirmDao=daoSession1.getTurnFirmDao();
        gma_pkidTurnoDao=daoSession1.getGMA_PKIDTurnoDao();

        databaseName.add(helper.getDatabaseName());
        entities.add(personaDao.getTablename());
        entities.add(ordenDao.getTablename());
        entities.add(materialDao.getTablename());
        entities.add(firmaDao.getTablename());
        entities.add(dibujoDao.getTablename());
        entities.add(comentarioDao.getTablename());
        entities.add(ordePersDao.getTablename());
        entities.add(ordeMateDao.getTablename());
        entities.add(fotografiaDao.getTablename());
        entities.add(gopOrdeatriDao.getTablename());
        entities.add(gmaCosttitrDao.getTablename());
    }
}
