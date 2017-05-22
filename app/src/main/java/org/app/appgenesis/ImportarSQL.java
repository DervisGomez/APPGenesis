package org.app.appgenesis;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.app.appgenesis.dao.GMA_CAUSAL;
import org.app.appgenesis.dao.GMA_CAUSALDao;
import org.app.appgenesis.dao.GMA_PKID;
import org.app.appgenesis.dao.GMA_PKIDDao;
import org.app.appgenesis.dao.Material;
import org.app.appgenesis.dao.MaterialDao;
import org.app.appgenesis.dao.Persona;
import org.app.appgenesis.dao.PersonaDao;
import org.app.atenciondeordenes.DAOApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dervis on 09/02/17.
 */
public class ImportarSQL {

    public void importarPersonalOrden(Context context) throws IOException {

        Log.d("LOG", "Importando base inicial ::: importarPersonalOrden()");
        long valIni = (new Date()).getTime();

        String s = new String();
        StringBuffer sb = new StringBuffer();

        InputStream is = null;
        is = context.getAssets().open("personal.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while((s = br.readLine()) != null){
            sb.append(s);
        }
        br.close();

        DAOApp daoApp=new DAOApp();
        PersonaDao personaDao=daoApp.getPersonaDao();
        ArrayList<Persona> personas = new ArrayList<>();

        try{
            String json = sb.toString();
            JsonParser parser = new JsonParser();
            Object obje = parser.parse(json);
            JsonArray array=(JsonArray)obje;
            if(!array.isJsonNull()) {
                personaDao.deleteAll();


                for (int x = 0; x < array.size(); x++) {
                    Persona persona=new Persona();
                    JsonObject objO = array.get(x).getAsJsonObject();
                    if (!(objO.get("perscons").isJsonNull())) {

                        List<Persona> personas1=personaDao.queryBuilder().where(PersonaDao.Properties.Perscons.in(objO.get("perscons").getAsInt())).list();
                        if (personas1.size()==0){
                            persona.setPerscons(objO.get("perscons").getAsInt());
                            if (!(objO.get("persaror").isJsonNull())) {
                                persona.setPersaror(objO.get("persaror").getAsInt());
                            }if (!(objO.get("persopid").isJsonNull())) {
                                persona.setPersopid(objO.get("persopid").getAsInt());
                            }if (!(objO.get("perstipo").isJsonNull())) {
                                persona.setPerstipo(objO.get("perstipo").getAsInt());
                            }if (!(objO.get("persnomb").isJsonNull())) {
                                persona.setPersnomb(objO.get("persnomb").getAsString());
                            }if (!(objO.get("perstiid").isJsonNull())) {
                                persona.setPerstiid(objO.get("perstiid").getAsInt());
                            }if (!(objO.get("persiden").isJsonNull())) {
                                persona.setPersiden(objO.get("persiden").getAsString());
                            }if (!(objO.get("persusop").isJsonNull())) {
                                persona.setPersusop(objO.get("persusop").getAsString());
                            }if (!(objO.get("persusua").isJsonNull())) {
                                persona.setPersusua((objO.get("persusua").getAsString()));
                            }if (!(objO.get("persesta").isJsonNull())) {
                                persona.setPersesta(objO.get("persesta").getAsString());
                            }if (!(objO.get("persuscr").isJsonNull())) {
                                persona.setPersuscr(objO.get("persuscr").getAsString());
                            }if (!(objO.get("persfecr").isJsonNull())) {
                                persona.setPersfecr(objO.get("persfecr").getAsString());
                            }

                            personas.add(persona);
                        }
                    }
                }

            }
        }catch (Exception e){
            Log.e("Excepcion", e.getMessage());
        }

        personaDao.insertInTx(personas);

        long valFin = (new Date()).getTime();
        Log.d("LOG", "Importación finalizada ::: importarPersonalOrden() ::: cantidad(" + personas.size() + ") ::: (" + ((valFin-valIni)/1000)  +" Sg)");

    }


    public void importarMaterialOrden(Context context) throws IOException {

        Log.d("LOG", "Importando base inicial ::: importarMaterialOrden()");
        long valIni = (new Date()).getTime();

        String s = new String();
        StringBuffer sb = new StringBuffer();

        InputStream is = null;
        is = context.getAssets().open("materiales.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while((s = br.readLine()) != null){
            sb.append(s);
        }
        br.close();

        //Apartir del JSON cargar materiales
        String json = sb.toString();
        ArrayList<Material> materiales = new ArrayList<>();
        try {
            JsonParser parser = new JsonParser();
            Object obje = parser.parse(json);
            JsonArray array=(JsonArray)obje;
            if(!array.isJsonNull()) {

                DAOApp daoApp=new DAOApp();
                MaterialDao materialDao=daoApp.getMaterialDao();
                materialDao.deleteAll();

                for (int x = 0; x < array.size(); x++) {
                    Material material = new Material();
                    JsonObject objO = array.get(x).getAsJsonObject();
                    if (!(objO.get("matecons").isJsonNull())) {
                        List<Material> materials = materialDao.queryBuilder().where(MaterialDao.Properties.Matecons.in(objO.get("matecons").getAsInt())).list();
                        if (materials.size() == 0) {
                            material.setMatecons(objO.get("matecons").getAsInt());
                            if (!(objO.get("matecodi").isJsonNull())) {
                                material.setMatecodi(objO.get("matecodi").getAsInt());
                            }
                            if (!(objO.get("matedesc").isJsonNull())) {
                                material.setMatedesc(objO.get("matedesc").getAsString());
                            }
                            if (!(objO.get("mateclas").isJsonNull())) {
                                material.setMateclas(objO.get("mateclas").getAsInt());
                            }
                            if (!(objO.get("mateunme").isJsonNull())) {
                                material.setMateunme(objO.get("mateunme").getAsInt());
                            }
                            if (!(objO.get("mategara").isJsonNull())) {
                                material.setMategara(objO.get("mategara").getAsString());
                            }
                            if (!(objO.get("mateexis").isJsonNull())) {
                                material.setMateexis(objO.get("mateexis").getAsString());
                            }
                            if (!(objO.get("matecost").isJsonNull())) {
                                material.setMatecost(objO.get("matecost").getAsString());
                            }
                            if (!(objO.get("mateesta").isJsonNull())) {
                                material.setMateesta((objO.get("mateesta").getAsInt()));
                            }
                            if (!(objO.get("mateuscr").isJsonNull())) {
                                material.setMateuscr(objO.get("mateuscr").getAsString());
                            }
                            if (!(objO.get("matefecr").isJsonNull())) {
                                material.setMatefecr(objO.get("matefecr").getAsString());
                            }

                            materiales.add(material);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Error: " , e.getMessage());
        }

        DAOApp daoApp=new DAOApp();
        MaterialDao materialDao=daoApp.getMaterialDao();
        materialDao.insertInTx(materiales);

        long valFin = (new Date()).getTime();
        Log.d("LOG", "Importación finalizada ::: importarMaterialOrden() ::: cantidad(" + materiales.size() + ") ::: ("+ ((valFin-valIni)/1000)  + " Sg)");

    }


    public void importarCausalOrden(Context context) throws IOException {

        Log.d("LOG", "Importando base inicial ::: importarCausalOrden()");
        long valIni = (new Date()).getTime();

        String s = new String();
        StringBuffer sb = new StringBuffer();

        InputStream is = null;
        is = context.getAssets().open("causales.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while((s = br.readLine()) != null){
            sb.append(s);
        }
        br.close();

        DAOApp daoApp=new DAOApp();
        GMA_CAUSALDao gma_causalDao = daoApp.getGMA_CAUSALDao();
        ArrayList<GMA_CAUSAL> causales = new ArrayList<>();

        try {
            String json = sb.toString();
            JsonParser parser = new JsonParser();
            Object obje = parser.parse(json);
            JsonArray array = (JsonArray) obje;
            if (!array.isJsonNull()) {
                gma_causalDao.deleteAll();

                for (int x = 0; x < array.size(); x++) {
                    GMA_CAUSAL causal = new GMA_CAUSAL();
                    JsonObject objO = array.get(x).getAsJsonObject();
                    if (!(objO.get("causcons").isJsonNull())) {
                        List<GMA_CAUSAL> causals = gma_causalDao.queryBuilder().where(GMA_CAUSALDao.Properties.Causcons.in(objO.get("causcons").getAsInt())).list();

                        if (causals.size() == 0) {
                            causal.setCauscons(objO.get("causcons").getAsString());
                            if (!(objO.get("causdesc").isJsonNull())) {
                                causal.setCausdesc(objO.get("causdesc").getAsString());
                            }
                            if (!(objO.get("caustipo").isJsonNull())) {
                                causal.setCaustipo(objO.get("caustipo").getAsString());
                            }
                            if (!(objO.get("causesta").isJsonNull())) {
                                causal.setCausesta(objO.get("causesta").getAsString());
                            }
                            if (!(objO.get("caususcr").isJsonNull())) {
                                causal.setCaususcr(objO.get("caususcr").getAsString());
                            }
                            if (!(objO.get("causfecr").isJsonNull())) {
                                causal.setCausfecr(objO.get("causfecr").getAsString());
                            }
                            causales.add(causal);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Excepcion: ", e.getMessage());
        }

        gma_causalDao.insertInTx(causales);

        long valFin = (new Date()).getTime();
        Log.d("LOG", "Importando base inicial ::: importaciónFinalizada() ::: Cantidad(" + causales.size() +") ::: ("+ ((valFin-valIni)/1000) +" Sg)");

    }

    public void importarDatosOrden(Context context) throws IOException {

        DAOApp daoApp=new DAOApp();
        GMA_CAUSALDao gma_causalDao = daoApp.getGMA_CAUSALDao();
        MaterialDao materialDao=daoApp.getMaterialDao();
        PersonaDao personaDao=daoApp.getPersonaDao();

        if(gma_causalDao.count() <= 0){
            importarCausalOrden(context);
        }
        if(personaDao.count() <= 0){
            importarPersonalOrden(context);
        }
        if(materialDao.count() <= 0){
            importarMaterialOrden(context);
        }

    }

    public void importarDatosTurno(Context context) throws IOException {
        DAOApp daoApp=new DAOApp();
        GMA_PKIDDao gma_pkidDao=daoApp.getGma_pkidDao();
        List<GMA_PKID> gma_pkids=gma_pkidDao.queryBuilder().where(GMA_PKIDDao.Properties.Tabla.in("MaterialTurno")).list();
        if (gma_pkids.size()>0){
        }else{
            //importarMaterial(context);
        }
    }
}
