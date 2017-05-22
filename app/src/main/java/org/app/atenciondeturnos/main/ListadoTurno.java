package org.app.atenciondeturnos.main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.app.appgenesis.Globals;
import org.app.appgenesis.ImportarSQL;
import org.app.appgenesis.R;
import org.app.appgenesis.dao.GMA_PKID;
import org.app.appgenesis.dao.GMA_PKIDDao;
import org.app.atenciondeordenes.DAOApp;
import org.app.atenciondeturnos.dao.GDB_TURNOS;
import org.app.atenciondeturnos.dao.GDB_TURNOSDao;
import org.app.atenciondeturnos.dao.GDB_TURNPERS;
import org.app.atenciondeturnos.dao.GDB_TURNPERSDao;
import org.app.atenciondeturnos.dao.GDB_VALVULA;
import org.app.atenciondeturnos.dao.GDB_VALVULADao;
import org.app.atenciondeturnos.dao.GMA_PKIDTurno;
import org.app.atenciondeturnos.dao.GMA_PKIDTurnoDao;
import org.app.atenciondeturnos.dao.TurnMate;
import org.app.atenciondeturnos.dao.TurnMateDao;
import org.app.atenciondeturnos.dao.TurnMateTurnDao;
import org.app.atenciondeturnos.dao.TurnPers;
import org.app.atenciondeturnos.dao.TurnPersDao;
import org.app.atenciondeturnos.dao.TurnPersTurnDao;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ListadoTurno extends AppCompatActivity {
    private ProgressDialog pd = null;
    private Globals globals = Globals.getInstance();
    private ListView lvAtencionTurno;
    int control=0;
    int preparar=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_turno);
        lvAtencionTurno=(ListView)findViewById(R.id.lvAtencionTurno);

        //llamar activity segun turno cliqueado
        lvAtencionTurno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long arg) {
                // Loads the given URL

                GDB_TURNOS item = (GDB_TURNOS) lvAtencionTurno.getAdapter().getItem(position);
                llamarActivity(item);

            }
        });
        ImportarSQL importarSQL=new ImportarSQL();
        try {
            importarSQL.importarDatosTurno(this);
        } catch (IOException e) {
            e.printStackTrace();
            showAlertDialog(this,"hola",e.toString(),true);
        }
        buscarTurnos();
    }

    //Metodo que llama a la activity que carga los datos del turno por pestaña
    public void llamarActivity(GDB_TURNOS item){
        Intent i=new Intent(this, DatosTurno.class);
        i.putExtra("id",item.getId());
        /*i.putExtra("titr",item.getORDETITR());

        Log.d("Enviar: ", item.getORDETITR()+"");

        //startActivity(i);*/
        startActivityForResult(i,1);
    }

    //Metodo que recarga el listado de turnos al regresar a esta activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        buscarTurnos();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_turno, menu);
        return true;
    }

    @Override//Acción segun opción de menu seleccionado
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int i = item.getItemId();
        if (i == R.id.descargar_turnos) {
            pd = ProgressDialog.show(this, "Proceso", "Descargando Turnos. Espere unos segundos...", true, false);
            cargarTurno(jsonTurno());
            return true;
        } else if (i==R.id.descargar_valvulas){
            preparar=0;
            descargarValvulas();
            return true;
        }if (i == R.id.descargar_personas_turno) {
            preparar=0;
            descargarPersonal();
            return true;
        } else if (i==R.id.descargar_materiales_turno){
            preparar=0;
            descargarMaterial();
            return true;
        }else if (i==R.id.preparar_datos_turno){
            preparar=1;
            descargarPersonal();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }

    }

    //metodo para descargar valvulas
    public void descargarValvulas(){
        pd = ProgressDialog.show(this, "Proceso", "Dergando Valvulas. Espere unos segundos...", true, false);
        cargarValvula(jsonValvulina());
    }

    //metodo descargar personal
    public void descargarPersonal(){
        this.pd = ProgressDialog.show(this, "Proceso", "Descargando Personal, por favor espere unos segundos...", true, false);
        control=2;
            JSONObject jsonObject = new JSONObject();
            DAOApp daoApp=new DAOApp();
            GMA_PKIDDao gma_pkidDao=daoApp.getGma_pkidDao();
            List<GMA_PKID> gma_pkids=gma_pkidDao.queryBuilder().where(GMA_PKIDDao.Properties.Tabla.in("PersonaOrden")).list();
            try {
                if (gma_pkids.size()>0){
                    jsonObject.put("cantidad",gma_pkids.get(0).getCantidad());
                }else{
                    GMA_PKID gma_pkid=new GMA_PKID();
                    gma_pkid.setTabla("PersonaOrden");
                    gma_pkid.setCantidad(0);
                    gma_pkidDao.insert(gma_pkid);
                    jsonObject.put("cantidad",0);
                }
                new MiTarea(globals.getServer()+"/GenadminOp/gmapersonal/listAll",jsonObject.toString()).execute();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        //new MiTarea(globals.getServer()+"/GenadminOp/gmapersonal/listAll","").execute();
    }

    //Metodo para descargar materiales
    public void descargarMaterial(){
        this.pd = ProgressDialog.show(this, "Proceso", "Descargando Materiales, por favor espere unos segundos...", true, false);
        control=3;
        JSONObject jsonObject = new JSONObject();
        DAOApp daoApp=new DAOApp();
        GMA_PKIDDao gma_pkidDao=daoApp.getGma_pkidDao();
        List<GMA_PKID> gma_pkids=gma_pkidDao.queryBuilder().where(GMA_PKIDDao.Properties.Tabla.in("MaterialOrden")).list();
        try {
            if (gma_pkids.size()>0){
                jsonObject.put("cantidad",gma_pkids.get(0).getCantidad());
            }else{
                GMA_PKID gma_pkid=new GMA_PKID();
                gma_pkid.setTabla("MaterialOrden");
                gma_pkid.setCantidad(0);
                gma_pkidDao.insert(gma_pkid);
                jsonObject.put("cantidad",0);
            }
            new MiTarea(globals.getServer()+"/GenadminOp/gmamaterial/listAll",jsonObject.toString()).execute();
        }catch (JSONException e) {
            e.printStackTrace();
        }
        //new MiTarea(globals.getServer()+"/GenadminOp/gmamaterial/listAll","").execute();
    }

    //metodo que guarda los datos del personal proveniente del servidor
    public void cargarPersona(String datos){
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    DAOApp daoApp=new DAOApp();
                    TurnPersDao personaDao=daoApp.getTurnPersDao();
                    personaDao.deleteAll();
                    GMA_PKIDTurnoDao gma_pkidDao=daoApp.getGma_pkidTurnoDao();
                    List<GMA_PKIDTurno> gma_pkids=gma_pkidDao.queryBuilder().where(GMA_PKIDTurnoDao.Properties.Tabla.in("PersonaTurno")).list();
                    int xx=gma_pkids.get(0).getCantidad();
                    gma_pkids.get(0).setCantidad(xx+array.size());
                    gma_pkidDao.update(gma_pkids.get(0));
                    for (int x = 0; x < array.size(); x++) {
                        TurnPers persona=new TurnPers();
                        JsonObject objO = array.get(x).getAsJsonObject();
                        if (!(objO.get("perscons").isJsonNull())) {
                            List<TurnPers> personas=personaDao.queryBuilder().where(TurnPersDao.Properties.Perscons.in(objO.get("perscons").getAsInt())).list();
                            if (personas.size()==0){
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
                                }/*if (!(objO.get("unoppers").isJsonNull())) {
                    persona.setUnoppers(objO.get("unoppers").getAsString());
                }if (!(objO.get("peuopers").isJsonNull())) {
                    persona.setPeuopers(objO.get("peuopers").getAsString());
                }*/
                                //DAOApp daoApp=new DAOApp();

                                personaDao.insert(persona);
                            }
                        }
                    }

                }
            }catch (Exception e){
                showAlertDialog(this, "Personas", e.toString(),false);
            }
        }else{
            showAlertDialog(this, "Orden", "No hay registros disponibles para descargar "+datos,false);
        }

        if (preparar==0){
            if (this.pd != null) {
                this.pd.dismiss();
            }
            showAlertDialog(this, "Orden", "Personas descargadas.",true);
        }else{
            descargarMaterial();
        }


    }

    //metodo que guarda los datos del material proveniente del servidor
    public void cargarMaterial(String datos){
        if(datos.length()>2){
            try {
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    DAOApp daoApp=new DAOApp();
                    TurnMateDao materialDao=daoApp.getTurnMateDao();
                    materialDao.deleteAll();
                    GMA_PKIDTurnoDao gma_pkidDao=daoApp.getGma_pkidTurnoDao();
                    List<GMA_PKIDTurno> gma_pkids=gma_pkidDao.queryBuilder().where(GMA_PKIDTurnoDao.Properties.Tabla.in("MaterialTurno")).list();
                    int xx=gma_pkids.get(0).getCantidad();
                    gma_pkids.get(0).setCantidad(xx+array.size());
                    gma_pkidDao.update(gma_pkids.get(0));
                    for (int x = 0; x < array.size(); x++) {
                        TurnMate material = new TurnMate();
                        JsonObject objO = array.get(x).getAsJsonObject();
                        if (!(objO.get("matecons").isJsonNull())) {
                            List<TurnMate> materials = materialDao.queryBuilder().where(TurnMateDao.Properties.Matecons.in(objO.get("matecons").getAsInt())).list();
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
                                }/*if (!(objO.get("mattmate").isJsonNull())) {
                            material.setMattmate(objO.get("mattmate").getAsString());
                        }*/

                                materialDao.insert(material);

                            }
                        }
                    }
                }
            } catch (Exception e) {
                showAlertDialog(this, "Material", e.toString(), false);
            }
        }else{
            showAlertDialog(this, "Orden", "No hay registros disponibles para descargar "+datos,false);
        }

        if (preparar==0){
            if (this.pd != null) {
                this.pd.dismiss();
            }
            showAlertDialog(this, "Orden", "Materiales descargados",true);
        }else{
            descargarValvulas();
        }


    }

    //metodo que guarda los datos del personal proveniente del servidor
    public void cargarMaterial(){
        TurnMateDao materialDao;
        DAOApp daoApp=new DAOApp();
        materialDao=daoApp.getTurnMateDao();
        for (int x = 0; x < 10; x++) {
            TurnMate material = new TurnMate();
            List<TurnMate> materials = materialDao.queryBuilder().where(TurnMateDao.Properties.Matecons.in(x)).list();
            if (materials.size() == 0) {
                material.setMatecons(x);
                material.setMatecodi(1);
                material.setMatedesc("Material"+String.valueOf(x));
                material.setMateclas(1);
                material.setMateunme(114);
                material.setMategara("prueba");
                material.setMateexis("prueba");
                material.setMatecost("prueba");
                material.setMateesta(1);
                material.setMateuscr("pruea");
                material.setMatefecr("Prueba");
                    /*if (!(objO.get("mattmate").isJsonNull())) {
                            material.setMattmate(objO.get("mattmate").getAsString());
                        }*/

                materialDao.insert(material);

            }

        }
        showAlertDialog(this, "Material", "Datos Descargados",false);
        if (this.pd != null) {
            this.pd.dismiss();
        }
    }

    public void cargarPersonal(){
        TurnPersDao personaDao;
        DAOApp d=new DAOApp();
        personaDao=d.getTurnPersDao();
        for (int x = 0; x < 10; x++) {
            TurnPers persona=new TurnPers();

            List<TurnPers> personas=personaDao.queryBuilder().where(TurnPersDao.Properties.Perscons.in(x)).list();
            if (personas.size()==0){
                persona.setPerscons(x);
                persona.setPersaror(1);
                persona.setPersopid(1);
                persona.setPerstipo(1);
                persona.setPersnomb("Dervis"+String.valueOf(x));
                persona.setPerstiid(1);
                persona.setPersiden("prueba");
                persona.setPersusop("prueba");
                persona.setPersusua("prueba");
                persona.setPersesta("prueba");
                persona.setPersuscr("prueba");
                persona.setPersfecr("prueba");
                personaDao.insert(persona);
            }

        }
        showAlertDialog(this, "Personal", "Datos Descargados",false);
        if (this.pd != null) {
            this.pd.dismiss();
        }
    }

    public String jsonTurno(){
        String json="";
        try{
            JSONArray items=new JSONArray();

            JSONObject item=new JSONObject();
            item.put("TURNCONS",1);
            item.put("TURNESTA","2");
            item.put("TURNFECH","19/01/2017 18:03");
            item.put("TURNSECT","1");
            item.put("TURNSEDE","Belén Alto: Sect. Pastelero (calles 31 a 35 avenidas 24 y 25), Sect. Subestación Centrales eléctricas (hasta la calle 22)");
            item.put("TURNZONA","2");
            item.put("TURNUSCR","wilfred.garcia");
            items.put(item);

            JSONObject item1=new JSONObject();
            item1.put("TURNCONS",2);
            item1.put("TURNESTA","2");
            item1.put("TURNFECH","19/01/2017 18:03");
            item1.put("TURNSECT","2");
            item1.put("TURNSEDE"," Belén Medio (sectores Maracaná alto, Belen av 26 y vía a Rudesindo Soto) , Los Yabos y sector Islandia de Barrio Nuevo");
            item1.put("TURNZONA","2");
            item1.put("TURNUSCR","wilfred.garcia");
            items.put(item1);

            JSONObject item2=new JSONObject();
            item2.put("TURNCONS",3);
            item2.put("TURNESTA","2");
            item2.put("TURNFECH","19/01/2017 18:03");
            item2.put("TURNSECT","3");
            item2.put("TURNSEDE","Belén Alto: Sect. Pastelero (calles 31 a 35 avenidas 24 y 25), Sect. Subestación Centrales eléctricas (hasta la calle 22)");
            item2.put("TURNZONA","2");
            item2.put("TURNUSCR","wilfred.garcia");
            items.put(item2);

            JSONObject item3=new JSONObject();
            item3.put("TURNCONS",4);
            item3.put("TURNESTA","2");
            item3.put("TURNFECH","19/01/2017 18:03");
            item3.put("TURNSECT","4");
            item3.put("TURNSEDE","B. La Pastora zona  alta (avenidas 29 a 34)");
            item3.put("TURNZONA","2");
            item3.put("TURNUSCR","wilfred.garcia");
            items.put(item3);

            json=items.toString();

        }catch (Exception e){

        }






        return json;
    }

    public String jsonValvulina(){
        String json="";
        try{
            JSONArray items=new JSONArray();

            JSONObject item=new JSONObject();
            item.put("VALVCONS",1);
            item.put("VALVDESC","VALVULA PORTICO");
            item.put("VALVDIRE","CALLE 11 #45-5 B. PORTICO");
            item.put("VALVDIAM","20");
            item.put("VALVCOOR","72.545");
            item.put("VALVESTA","2");
            item.put("VALVUSCR","wilfred.garcia");
            item.put("VALVFECR","19/01/2017 18:14");
            items.put(item);

            JSONObject item1=new JSONObject();
            item1.put("VALVCONS",2);
            item1.put("VALVDESC","VALVULA TERMOTASAJERO");
            item1.put("VALVDIRE","CALLE 2 #45-1 TERMOTASAJERO");
            item1.put("VALVDIAM","15");
            item1.put("VALVCOOR","72.545");
            item1.put("VALVESTA","2");
            item1.put("VALVUSCR","wilfred.garcia");
            item1.put("VALVFECR","19/01/2017 18:14");
            items.put(item1);

            JSONObject item2=new JSONObject();
            item2.put("VALVCONS",3);
            item2.put("VALVDESC","VALVULA LA LIBERTAD");
            item2.put("VALVDIRE","AVENIDA 6 #4.54 LA LIBERTAD");
            item2.put("VALVDIAM","10");
            item2.put("VALVCOOR","72.545");
            item2.put("VALVESTA","2");
            item2.put("VALVUSCR","wilfred.garcia");
            item2.put("VALVFECR","19/01/2017 18:14");
            items.put(item2);

            json=items.toString();

        }catch (Exception e){

        }
        return json;
    }

    //metodo que carga la lista de turnos
    public void buscarTurnos(){
        List<GDB_TURNOS> gdb_turnoses=new ArrayList<GDB_TURNOS>();
        DAOApp daoAppTurno=new DAOApp();
        GDB_TURNOSDao gdb_turnosDao=daoAppTurno.getGdb_turnosDao();
        //gdb_turnoses=gdb_turnosDao.loadAll();
        try{
            gdb_turnoses=gdb_turnosDao.queryBuilder().where(GDB_TURNOSDao.Properties.Descargado.in(globals.getUsuario_dominio())).list();
        }catch (Exception e){
            showAlertDialog(this, "Orden", e.toString(),false);
        }

        if(gdb_turnoses.size()>0){
            this.lvAtencionTurno.setAdapter(new TurnosAdapter(this, gdb_turnoses));
        }else{
            showAlertDialog(this, "Orden", "No hay datos para mostrar",false);
            Log.d("Información", "No hay datos para mostrar");
        }
    }

    //metodo que guarda los datos de valvulas proveniente del servidor
    public void cargarValvula(String datos){
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    DAOApp daoAppTurno=new DAOApp();
                    GDB_VALVULADao dao=daoAppTurno.getGdb_valvulaDao();
                    for (int x = 0; x < array.size(); x++) {
                        GDB_VALVULA item=new GDB_VALVULA();
                        JsonObject objO = array.get(x).getAsJsonObject();
                        if (!(objO.get("VALVCONS").isJsonNull())) {
                            List<GDB_VALVULA > lista=dao.queryBuilder().where(GDB_VALVULADao.Properties.VALVCONS.in(objO.get("VALVCONS").getAsLong())).list();
                            if (lista.size()==0){
                                item.setVALVCONS(objO.get("VALVCONS").getAsLong());
                                if (!(objO.get("VALVDESC").isJsonNull())) {
                                    item.setVALVDESC(objO.get("VALVDESC").getAsString());
                                }if (!(objO.get("VALVDIRE").isJsonNull())) {
                                    item.setVALVDIRE(objO.get("VALVDIRE").getAsString());
                                }if (!(objO.get("VALVDIAM").isJsonNull())) {
                                    item.setVALVDIAM(objO.get("VALVDIAM").getAsString());
                                }if (!(objO.get("VALVCOOR").isJsonNull())) {
                                    item.setVALVCOOR(objO.get("VALVCOOR").getAsString());
                                }if (!(objO.get("VALVESTA").isJsonNull())) {
                                    item.setVALVESTA(objO.get("VALVESTA").getAsString());
                                }if (!(objO.get("VALVUSCR").isJsonNull())) {
                                    item.setVALVUSCR(objO.get("VALVUSCR").getAsString());
                                }if (!(objO.get("VALVFECR").isJsonNull())) {
                                    item.setVALVFECR(objO.get("VALVFECR").getAsString());
                                }
                                dao.insert(item);
                            }
                        }
                    }

                }
            }catch (Exception e){
                showAlertDialog(this, "Turno", e.toString(),false);
            }
        }else{
            showAlertDialog(this, "Turno", "No hay registros disponibles para descargar "+datos,false);
        }

        if (preparar==0){
            showAlertDialog(this, "Turno", "Valvulas Descargadas Exitosamente",false);
        }else{
            showAlertDialog(this, "Turno", "Registros Descargados Exitosamente",false);
        }

        if (this.pd != null) {
            this.pd.dismiss();
        }
    }

    public void cargarPers(String datos){
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    DAOApp daoAppTurno=new DAOApp();
                    GDB_TURNPERSDao dao=daoAppTurno.getGdb_turnpersDao();
                    for (int x = 0; x < array.size(); x++) {
                        GDB_TURNPERS item=new GDB_TURNPERS();
                        JsonObject objO = array.get(x).getAsJsonObject();
                        if (!(objO.get("TUPECONS").isJsonNull())) {
                            List<GDB_TURNPERS> lista=dao.queryBuilder().where(GDB_TURNPERSDao.Properties.TUPECONS.in(objO.get("TUPECONS").getAsLong())).list();
                            if (lista.size()==0){
                                item.setTUPECONS(objO.get("TUPECONS").getAsLong());
                                if (!(objO.get("TUPETURN").isJsonNull())) {
                                    item.setTUPETURN(objO.get("TUPETURN").getAsLong());
                                }if (!(objO.get("TUPEPERS").isJsonNull())) {
                                    item.setTUPEPERS(objO.get("TUPEPERS").getAsString());
                                }if (!(objO.get("TUPEFEIN").isJsonNull())) {
                                    item.setTUPEFEIN(objO.get("TUPEFEIN").getAsString());
                                }if (!(objO.get("TUPEFEFI").isJsonNull())) {
                                    item.setTUPEFEFI(objO.get("TUPEFEFI").getAsString());
                                }if (!(objO.get("TUPEOBSE").isJsonNull())) {
                                    item.setTUPEOBSE(objO.get("TUPEOBSE").getAsString());
                                }if (!(objO.get("TUPEINIC").isJsonNull())) {
                                    item.setTUPEINIC(objO.get("TUPEINIC").getAsString());
                                }if (!(objO.get("TUPEFINA").isJsonNull())) {
                                    item.setTUPEFINA(objO.get("TUPEFINA").getAsString());
                                }if (!(objO.get("TUPEESTA").isJsonNull())) {
                                    item.setTUPEESTA(objO.get("TUPEESTA").getAsString());
                                }if (!(objO.get("TUPEUSCR").isJsonNull())) {
                                    item.setTUPEUSCR(objO.get("TUPEUSCR").getAsString());
                                }if (!(objO.get("TUPEFECR").isJsonNull())) {
                                    item.setTUPEFECR(objO.get("TUPEFECR").getAsString());
                                }
                                dao.insert(item);
                            }
                        }
                    }
                    showAlertDialog(this, "Turno", "Registros Descargados Exitosamente",false);
                }
            }catch (Exception e){
                showAlertDialog(this, "Turno", e.toString(),false);
            }
        }else{
            showAlertDialog(this, "Turno", "No hay registros disponibles para descargar "+datos,false);
        }

        if (this.pd != null) {
            this.pd.dismiss();
        }
    }

    //metodo que guarda los datos de turno proveniente del servidor
    public void cargarTurno(String datos){
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                if(!array.isJsonNull()) {
                    int num=0;
                    DAOApp daoAppTurno=new DAOApp();
                    GDB_TURNOSDao dao=daoAppTurno.getGdb_turnosDao();
                    for (int x = 0; x < array.size(); x++) {
                        GDB_TURNOS item=new GDB_TURNOS();
                        JsonObject objO = array.get(x).getAsJsonObject();
                        if (!(objO.get("TURNCONS").isJsonNull())) {
                            List<GDB_TURNOS> gdb_turnoses=dao.queryBuilder().where(GDB_TURNOSDao.Properties.TURNCONS.in(objO.get("TURNCONS").getAsLong())).list();
                            if (gdb_turnoses.size()==0){
                                item.setTURNCONS(objO.get("TURNCONS").getAsLong());
                                if (!(objO.get("TURNESTA").isJsonNull())) {
                                    item.setTURNESTA(objO.get("TURNESTA").getAsString());
                                }if (!(objO.get("TURNFECH").isJsonNull())) {
                                    item.setTURNFECR(objO.get("TURNFECH").getAsString());
                                }if (!(objO.get("TURNSECT").isJsonNull())) {
                                    item.setTURNSECT(objO.get("TURNSECT").getAsString());
                                }if (!(objO.get("TURNSEDE").isJsonNull())) {
                                    item.setTURNSEDE(objO.get("TURNSEDE").getAsString());
                                }if (!(objO.get("TURNZONA").isJsonNull())) {
                                    item.setTURNZONA(objO.get("TURNZONA").getAsString());
                                }if (!(objO.get("TURNUSCR").isJsonNull())) {
                                    item.setTURNUSCR(objO.get("TURNUSCR").getAsString());
                                }
                                item.setDescargado(globals.getUsuario_dominio());
                                dao.insert(item);
                                num++;
                            }
                        }
                    }
                    showAlertDialog(this, "Turno", "Registros Descargados Exitosamente",false);
                }
            }catch (Exception e){
                showAlertDialog(this, "Turno", e.toString(),false);
            }
            buscarTurnos();
        }else{
            showAlertDialog(this, "Turno", "No hay registros disponibles para descargar "+datos,false);
        }

        if (this.pd != null) {
            this.pd.dismiss();
        }
    }

    //Metodo para mostrar alertas
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
//		alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
        alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }

    //Clase utilizada para consumir servicio
    private class MiTarea extends AsyncTask<String, Float, String> {
        private String ur;

        public MiTarea(String url,String x){
            Log.d("url",url);
            this.ur=url+x;

        }
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... urls) {
            String responce = "";

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet(ur);
            del.setHeader("content-type", "application/json");
            Log.d("Header",  globals.getToken_type() + " " + globals.getAccess_token());
            del.setHeader("Authorization", globals.getToken_type() + " " + globals.getAccess_token());

            try
            {
                HttpResponse resp = httpClient.execute(del);
                responce = EntityUtils.toString(resp.getEntity());
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
            }
            return responce;
        }

        @Override
        protected void onProgressUpdate (Float... valores) {

        }

        @Override
        protected void onPostExecute(String tiraJson) {
            if(control==1){
                //cargarDatos(tiraJson);
            }else if(control==2){
                cargarPersona(tiraJson);
            }else if(control==3){
                cargarMaterial(tiraJson);
            }else if(control==4){
                //cargarCausal(tiraJson);
            }

        }
    }


    private class MiTareaPost extends AsyncTask<String, Float, String> {
        private String jsonObject;
        private final String HTTP_EVENT;
        private HttpClient httpclient;
        BufferedReader in = null;

        public MiTareaPost(String url,String jsonObject){
            this.HTTP_EVENT=url;
            this.jsonObject=jsonObject;
        }
        protected void onPreExecute() {

        }

        protected String doInBackground(String... urls){
            String resul = "";
            try {

                //Creamos un objeto Cliente HTTP para manejar la peticion al servidor
                HttpClient httpClient = new DefaultHttpClient();
                //Creamos objeto para armar peticion de tipo HTTP POST
                HttpPost post = new HttpPost(HTTP_EVENT);

                //Configuramos los parametos que vaos a enviar con la peticion HTTP POST
                StringEntity stringEntity = new StringEntity(jsonObject);
                post.setHeader("Content-type", "application/json");
                Log.d("Header",  globals.getToken_type() + " " + globals.getAccess_token());
                post.setHeader("Authorization", globals.getToken_type() + " " + globals.getAccess_token());
                //post.setHeader("Authorization", "bearer eNoFwceia0AAANC9X8lCb4u3CNF7SQw7RBkXowdf/875ZmFYqI6QSL9BE1KZzhd2fQVlfEXBzf8BYIdP31CzTtzKWjRUOtOXGDCewt6/55bHLVd/8NIwfJqvMJytdrtZpoGnRsBZP/Tu+rCz8vaPrtIabYLduJCe98elaMOtnefReGxazc2U2Cd0uUIaKImp98kFESZ6NjtWhaBuWT/aa9ZM4ZuvK/8UWnlaiBg4WX+Uqf72+RNa9GlUMVFzrDg1eLrgwi3VpTkSbsDpwMV6X2437pIPvLCNXJei9D0TA35s77xN4D6+Qrz0A3bK1kbqK/xrUef2zQdLtWgDQiA+f6gevDJjq0vAhCiPIC0lVWEz5pc57uJjIzdrXmU7Cohb5sJn9aZTgnELD/OcBOCm70yhhKtdC5BMsI22y0A9HjgWprPdiO9w2K8A0rYx8MXFNmpX3c+P8tpPHm8ZankrxDmxsj0l5syvKvgr698FL1Kb7WZI2Jn59dTTEjDLXL+1VsijxEjdESyomN9LP3T1eKCWsq79OXqD9vr5fw9brkhvhVyWw43xxSn8jgMA68OjQPQmz8eGITk0ifATxQJIHA2PlDgqGX1fhtkKiCT6HhZXLnNq0awWNCqTJ3baPd0/WS8PUPpncvk6p0S59lk9EQs2BTKEnidqAwkPWjfKY6sTJQe/qceNpznqeHu3NLIiUOZPbNlMq/SHa0QZb6Yqv3SJKI7JuHePijDFoYQu/HRXWnO//dOHhG2xp/ZEDh9XFbkY7WcVteQgZ4CkBKJjR+0vNnoz1kto7O5DSeEMtLnfSBEbwiVxyaRMv71aorFKjeI6NdpnqnJaBgLhPas9WFNyhdQIT7Zm8RbBadTiOpXk/BJd/MyvWRdNg4yx+XCgzS8O6JsIv8dVjAhX9EJ7PXpynsIG1IpzEsC5BolrFPEADpK62vpD1ctsxo2jli1brkX5QgtiZFQEs3E1/DS2C04WDjCkTsiNFKcDjWRMOEiPnilSpU1OVNHnHlrrPCbeq+3JeEWhDMcr//379x8IoE8Z");

                post.setEntity(stringEntity);

                //Se ejecuta el envio de la peticion y se espera la respuesta de la misma.
                HttpResponse response = httpClient.execute(post);
//   			Log.w(APP_TAG, response.getStatusLine().toString());

                //Obtengo el contenido de la respuesta en formato InputStream Buffer y la paso a formato String
                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
                resul=sb.toString();

            } catch (ClientProtocolException e) {
                Log.e("Error",e.getMessage());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.e("Error",e.getMessage());
            }
            return resul;
        }

        protected void onProgressUpdate (Float... valores) {

        }

        protected void onPostExecute(String tiraJson) {
            //ACCION
        }


        private StringBuilder inputStreamToString(InputStream is) {
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader rd = new BufferedReader( new InputStreamReader(is) );
            try{
                while( (line = rd.readLine()) != null ){
                    stringBuilder.append(line);
                }
            }catch( IOException e){
                Log.e("Error",e.getMessage());
            }
            return stringBuilder;
        }
    }
}
