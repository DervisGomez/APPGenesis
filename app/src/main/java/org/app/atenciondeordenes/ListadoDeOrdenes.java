package org.app.atenciondeordenes;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

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
import org.app.appgenesis.dao.*;
import org.app.appgenesis.dao.Material;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.app.appgenesis.R;
import org.app.appgenesis.dao.Persona;
import org.app.atenciondeordenes.fragment_ii_atributos.Formulario;
import org.app.atenciondeordenes.fragment_ii_atributos.FormularioImpl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ListadoDeOrdenes extends AppCompatActivity implements View.OnClickListener{
    private ListView listView;
    private ProgressDialog pd = null;
    private Button btnOrdenarAccion;
    private Button btnOrdenarRegresar;
    private RelativeLayout formularioOrdenar;
    private Spinner spOrdenarPor;
    private RelativeLayout formularioFiltrar;
    List<Gro_orden> listaOrden=new ArrayList<Gro_orden>();

    private Button btnFiltrarRegresar;
    private Button btnFiltrarAccion;
    private EditText porOrden;
    private Spinner porEstado;
    private EditText porContrato;
    private EditText porNumeroD;
    private EditText porBarrio;
    private EditText porRuta;
    private Spinner efectividad;
    private Spinner porTipoD;
    private EditText porTipoT;
    private boolean controlLista=true;
    private Button btnVerTodas;
    private Globals globals = Globals.getInstance();
    private int preparar=0;
    List<String> ordetitrs =new ArrayList<String>();
    int sigatr=0;

    int control=0;

    private Gro_ordenDao ordenDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        this.listView = (ListView) findViewById(R.id.listView);
        btnOrdenarAccion=(Button)findViewById(R.id.btnOrdenOrdenarAccion);
        btnOrdenarRegresar=(Button)findViewById(R.id.btnOrdenOrdenarRegresar);
        formularioOrdenar=(RelativeLayout)findViewById(R.id.ordenFormularioOrdenar);
        formularioFiltrar=(RelativeLayout)findViewById(R.id.ordenFormularioFiltrar);
        spOrdenarPor=(Spinner)findViewById(R.id.spOrdenOrdenarPor);

        btnFiltrarRegresar=(Button)findViewById(R.id.btnOrdenFiltrarRegresar);
        btnFiltrarAccion=(Button)findViewById(R.id.btnOrdenFiltrarAccion);
        porOrden=(EditText)findViewById(R.id.etOrdenFiltrarOrden);
        porEstado=(Spinner)findViewById(R.id.spOrdenFiltrarEstado);
        porContrato=(EditText)findViewById(R.id.etOrdenFiltrarContrato);
        porNumeroD=(EditText)findViewById(R.id.etOrdenFiltrarTipo);
        porBarrio=(EditText)findViewById(R.id.etOrdenFiltrarBarrio);
        porRuta=(EditText)findViewById(R.id.etOrdenFiltrarRuto);
        efectividad=(Spinner)findViewById(R.id.spOrdenFiltrarEfectividad);
        porTipoT=(EditText)findViewById(R.id.etOrdenFiltrarTipoT);
        porTipoD=(Spinner)findViewById(R.id.spOrdenFiltrarTipoD);
        btnVerTodas=(Button)findViewById(R.id.btnVerTodas);
        btnVerTodas.setOnClickListener(this);

        btnFiltrarRegresar.setOnClickListener(this);
        btnFiltrarAccion.setOnClickListener(this);
        String[] t1={"Por Estado","Pendiente","Iniciada","Suspendida","Cerrada","Sincronizada"};
        ArrayAdapter<String> spFormatoAdaptener1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,t1);
        porEstado.setAdapter(spFormatoAdaptener1);
        String[] t2={"Por Efectividad","Efectiva","Inefectiva"};
        ArrayAdapter<String> spFormatoAdaptener2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,t2);
        efectividad.setAdapter(spFormatoAdaptener2);
        String[] t3={"Por Tipo de Documento","SOLICITUD","QUEJA","RECLAMO","ORDEN","DANO"};
        ArrayAdapter<String> spFormatoAdaptener3=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,t3);
        porTipoD.setAdapter(spFormatoAdaptener3);

        String[] t0={"Ordenar por","orden","estado","contrato","tipo de documento","numero de documento","tipo de trabajo","barrio","ruta","efectividad"};
        ArrayAdapter<String> spFormatoAdaptener=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,t0);
        spOrdenarPor.setAdapter(spFormatoAdaptener);
        formularioOrdenar.setVisibility(View.GONE);
        formularioFiltrar.setVisibility(View.GONE);
        btnOrdenarAccion.setOnClickListener(this);
        btnOrdenarRegresar.setOnClickListener(this);

        DAOApp d=new DAOApp();
        ordenDao=d.getGro_ordenDao();

        buscarOrdenes();

        //Llama a la orden que se le hace click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long arg) {
                // Loads the given URL
                if(controlLista){
                    Gro_orden item = (Gro_orden) listView.getAdapter().getItem(position);
                    llamarActivity(item);

                }

            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Globals g=Globals.getInstance();
        if(g.getAccess_token()==null){
            finish();
        }
        buscarOrdenes();
    }

    //Llama a la Activiy con las petañas referente a la orden qeu se le hizo click
    public void llamarActivity(Gro_orden item){
        Intent i=new Intent(this, DatosOrden.class);
        i.putExtra("id",item.getId());
        i.putExtra("titr",item.getORDETITR());

        Log.d("Enviar: ", item.getORDETITR()+"");

        //startActivity(i);
        startActivityForResult(i,1);
    }

    @Override //Carga el menu
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.main1, menu);
        return true;
    }

    @Override //Acción a ejecutar segun se hace click en el menú
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int i = item.getItemId();
        if (i == R.id.descargar_orden) {
            //Verficicar si tiene conexion wifi o datos
            boolean conexion=new VerificarConexion(this).estaConectado();
            if(conexion){
                this.pd = ProgressDialog.show(this, "Proceso", "Descargando Ordenes, por favor espere unos segundos...", true, false);
                control=1;
                JSONObject jsonObject = new JSONObject();
                int xx = Integer.valueOf(globals.getUsuario_cons());
                try {
                    jsonObject.put("orpepers",xx);
                    new MiTareaPost(globals.getServer()+"/Gendanos/goporden/ordenUsuario",jsonObject.toString()).execute();
                } catch (JSONException e) {
                    Log.d("Error ::: " , e.getMessage());
                    e.printStackTrace();
                }
            }else{
                showAlertDialog(this, "Error", "No tienes Conexión en este momento",false);
            }

            return true;
        //} else if(i==R.id.preparar_datos){
            /*this.pd = ProgressDialog.show(this, "Proceso", "Descargando Datos, por favor espere unos segundos...", true, false);
            preparar=0;
            //preparar =1;
            ImportarSQL miImport = new ImportarSQL();
            try {
                miImport.importarDatosOrden(this);
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.descargarCausales();
            this.descargarPersonal();
            this.descargarMateriales();
            this.descargarAtributos();

            showAlertDialog(this, "Orden", "Datos Descargados exitosamente",false);

            if(this.pd!=null){
                this.pd.dismiss();
            }*/

            //return true;
        }else if (i==R.id.filtrar_ordenes){
            formularioFiltrar.setVisibility(View.VISIBLE);
            controlLista=false;
            return true;
        }else if (i==R.id.ordenar_ordenes){
            formularioOrdenar.setVisibility(View.VISIBLE);
            controlLista=false;
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }

    }

    //Prepara la descarga de los datos (Causales, materiales, personal y atributos)
    public void prepararDatos(){
        // this.pd = ProgressDialog.show(this, "Proceso", "Descargando Datos, por favor espere unos segundos...", true, false);
        // preparar=0;
        //preparar =1;
        ImportarSQL miImport = new ImportarSQL();
        try {
            miImport.importarDatosOrden(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Verficicar si tiene conexion wifi o datos
        boolean conexion=new VerificarConexion(this).estaConectado();
            if(conexion){
                this.descargarCausales();
                this.descargarPersonal();
                this.descargarMateriales();
                this.descargarAtributos();
            }else{
                //showAlertDialog(this, "Error", "No tienes Conexión en este momento",false);
            }

        // showAlertDialog(this, "Orden", "Datos Descargados exitosamente",false);

    }

    //Metodo que descarga los Atributos
    public void descargarAtributos(){

            control=5;
            for (int x=0;x<listaOrden.size();x++){
                String titr=listaOrden.get(x).getORDETITR();
                boolean gua=true;
                for (int y=0;y<ordetitrs.size();y++){
                    if (titr.equalsIgnoreCase(ordetitrs.get(y).toString())){
                        gua=false;
                        y=ordetitrs.size();
                    }
                }
                if (gua){
                    Log.d("Guardar", "Guarda tipo de trabajo " + titr);
                    ordetitrs.add(titr);
                }
            }

            sigatr=0;
            JSONObject jsonObject = new JSONObject();
            try {
                if(!ordetitrs.isEmpty()){
                    jsonObject.put("titrcons",ordetitrs.get(sigatr).toString());

                    new MiTareaPost(globals.getServer()+"/Gendanos/goporden/atributosOrden",jsonObject.toString()).execute();

                }else{
                    Log.d("Atributos", "ordetitrs.isEmpty()");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    //Metodo que guarda los atributos traidos del servidor
    public void cargarAtributos(String json){
        //Verficicar si tiene conexion wifi o datos
        boolean conexion=new VerificarConexion(this).estaConectado();
        if(conexion){
            try {

                Log.d("Llega con JSOn ::: " , json);
                DAOApp daoapp=new DAOApp();
                GOP_ATRIBUTOSDao gop = daoapp.getGop_atributosDao();
                //JSONArray jsonArray = new JSONArray(json);

                //for (int i=0; i< jsonArray.length() ;i++){
                Log.d("Llega con JSOn ::: " , "Entra al FOR");
                GOP_ATRIBUTOS gop_ordeatri=new GOP_ATRIBUTOS();
                gop_ordeatri.setJson(json);
                gop_ordeatri.setTitr(ordetitrs.get(sigatr));
                gop.insert(gop_ordeatri);

                Log.e("Atributos", "Atributos cargados para TiTr " + ordetitrs.get(sigatr) );
                //}


                //Si necesita cargar más atributos para tipos de trabaj

                sigatr++;
                if(sigatr < ordetitrs.size()){

                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("titrcons",ordetitrs.get(sigatr));
                    new MiTareaPost(globals.getServer()+"/Gendanos/goporden/atributosOrden",jsonObject.toString()).execute();

                }

            }catch (Exception e){
                Log.e("Error", "Ha ocurrido una excepción: " + e.getMessage());
                // e.printStackTrace();
            }
        }else{
            showAlertDialog(this, "Error", "No tienes Conexión en este momento",false);
        }
    }

    //Metodo para descargar personal
    public void descargarPersonal(){
            control=2;
            //new MiTarea(globals.getServer()+"/GenadminOp/gmapersonal/listAll",jsonObject.toString()).execute();

    }

    //Metodo para descargar materiales
    public void descargarMateriales(){

            control=3;
            //new MiTarea(globals.getServer()+"/GenadminOp/gmamaterial/listAll",jsonObject.toString()).execute();

    }
    //Metodo para descargar causales
    public void descargarCausales(){
            control=4;
            //new MiTarea(globals.getServer()+"/GenadminOp/gmacausal/listAll",jsonObject.toString()).execute();
    }

    //Metodo que busca las ordenes correspondientes al usuario que inicia sesión
    public void buscarOrdenes(){
        List<Gro_orden> aaa=new ArrayList<Gro_orden>();
        Globals g=Globals.getInstance();
        if(g.getAccess_token()==null){
            finish();
        }
        try{
            aaa= ordenDao.queryBuilder().where(Gro_ordenDao.Properties.Descargado.in(globals.getUsuario_dominio())).list();
        }catch (Exception e){
            showAlertDialog(this, "Orden", e.toString(),false);
        }
        listaOrden.clear();
        //valida que las ordenes a mostrar, es decir que no este cerrada ni sincronizada
        for(int x=0;x<aaa.size();x++){
            if(!aaa.get(x).getESTADO().equals("Cerrada")&&!aaa.get(x).getESTADO().equals("Sincronizada")){
                listaOrden.add(aaa.get(x));
            }
        }
        //llama al adaptener para enlistar las ordenes en la interfaz
        if(listaOrden.size()>0){
            this.listView.setAdapter(new OrdenAdapter(this, listaOrden));
            this.prepararDatos();
        }else{
            showAlertDialog(this, "Orden", "No hay datos para mostrar",false);
            Log.d("Información", "No hay datos para mostrar");
        }
    }

    //Metodo que guarda los datos de la orden traidos del servidor
    public void cargarDatos(String datos){

        if(datos.length()>2){
            int cant=0;
            try {
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                //Valida que existan datos
                if(!array.isJsonNull()) {
                    List<Gro_orden> listaOrdens=new ArrayList<Gro_orden>();
                    //Ciclo que va guardando cada registro en la lista
                    for (int x = 0; x < array.size(); x++) {
                        Gro_orden orden = new Gro_orden();
                        JsonObject objO = array.get(x).getAsJsonObject();
                        if (!(objO.get("ORDECONS").isJsonNull())) {
                            List<Gro_orden> ordens = ordenDao.queryBuilder().where(Gro_ordenDao.Properties.ORDECONS.in(objO.get("ORDECONS").getAsInt())).list();
                            if (ordens.size() == 0) {
                                orden.setORDECONS(objO.get("ORDECONS").getAsInt());
                                if (!(objO.get("ORDEORAM").isJsonNull())) {
                                    orden.setORDEORAM(objO.get("ORDEORAM").getAsString());
                                }
                                if (!(objO.get("ORDECONT").isJsonNull())) {
                                    orden.setORDECONT(objO.get("ORDECONT").getAsString());
                                }
                                if (!(objO.get("ORDECAUS").isJsonNull())) {
                                    orden.setORDECAUS(objO.get("ORDECAUS").getAsString());
                                }
                                if (!(objO.get("ORDETIDO").isJsonNull())) {
                                    orden.setORDETIDO(objO.get("ORDETIDO").getAsString());
                                }
                                if (!(objO.get("ORDENUDO").isJsonNull())) {
                                    orden.setORDENUDO(objO.get("ORDENUDO").getAsString());
                                }
                                if (!(objO.get("ORDEPADR").isJsonNull())) {
                                    orden.setORDEPADR(objO.get("ORDEPADR").getAsString());
                                }
                                if (!(objO.get("ORDEFIEJ").isJsonNull())) {
                                    orden.setORDEFIEJ(objO.get("ORDEFIEJ").getAsString());
                                }
                                if (!(objO.get("ORDEFFEJ").isJsonNull())) {
                                    orden.setORDEFFEJ(objO.get("ORDEFFEJ").getAsString());
                                }
                                if (!(objO.get("ORDEFEEJ").isJsonNull())) {
                                    orden.setORDEFEEJ(objO.get("ORDEFEEJ").getAsString());
                                }
                                if (!(objO.get("ORDEFEAS").isJsonNull())) {
                                    orden.setORDEFEAS(objO.get("ORDEFEAS").getAsString());
                                }
                                if (!(objO.get("ORDEFELE").isJsonNull())) {
                                    orden.setORDEFELE(objO.get("ORDEFELE").getAsString());
                                }
                                if (!(objO.get("ORDEVALU").isJsonNull())) {
                                    orden.setORDEVALU(objO.get("ORDEVALU").getAsString());
                                }
                                if (!(objO.get("ORDEUNOP").isJsonNull())) {
                                    orden.setORDEUNOP(objO.get("ORDEUNOP").getAsString());
                                }
                                if (!(objO.get("ORDETITR").isJsonNull())) {
                                    orden.setORDETITR(objO.get("ORDETITR").getAsString());
                                }
                                if (!(objO.get("ORDECALE").isJsonNull())) {
                                    orden.setORDECALE(objO.get("ORDECALE").getAsString());
                                }
                                if (!(objO.get("ORDEOBSE").isJsonNull())) {
                                    orden.setORDEOBSE(objO.get("ORDEOBSE").getAsString());
                                }
                                if (!(objO.get("ORDEFELE").isJsonNull())) {
                                    orden.setORDEFELE(objO.get("ORDEFELE").getAsString());
                                }
                                if (!(objO.get("ORDEEFEC").isJsonNull())) {
                                    orden.setORDEEFEC(objO.get("ORDEEFEC").getAsString());
                                }
                                if (!(objO.get("ORDETERM").isJsonNull())) {
                                    orden.setORDETERM(objO.get("ORDETERM").getAsString());
                                }
                                if (!(objO.get("ORDELATI").isJsonNull())) {
                                    orden.setORDELATI(objO.get("ORDELATI").getAsString());
                                }
                                if (!(objO.get("ORDELONG").isJsonNull())) {
                                    orden.setORDELONG(objO.get("ORDELONG").getAsString());
                                }
                                if (!(objO.get("ORDEALTI").isJsonNull())) {
                                    orden.setORDEALTI(objO.get("ORDEALTI").getAsString());
                                }
                                if (!(objO.get("ORDEESTA").isJsonNull())) {
                                    orden.setORDEESTA(objO.get("ORDEESTA").getAsString());
                                }
                                if (!(objO.get("ORDEUSCR").isJsonNull())) {
                                    orden.setORDEUSCR(objO.get("ORDEUSCR").getAsString());
                                }
                                if (!(objO.get("ORDEFECR").isJsonNull())) {
                                    orden.setORDEFECR(objO.get("ORDEFECR").getAsString());
                                }
                                if (!(objO.get("UNOPNOMB").isJsonNull())) {
                                    orden.setUNOPNOMB(objO.get("UNOPNOMB").getAsString());
                                }
                                if (!(objO.get("TITRDESC").isJsonNull())) {
                                    orden.setTITRDESC(objO.get("TITRDESC").getAsString());
                                }
                                if (!(objO.get("ORESESTA").isJsonNull())) {
                                    orden.setORESESTA(objO.get("ORESESTA").getAsString());
                                }
                                if (!(objO.get("TIPODOCU").isJsonNull())) {
                                    orden.setTIPODOCU(objO.get("TIPODOCU").getAsString());
                                }
                                if (!(objO.get("CAUSLEGA").isJsonNull())) {
                                    orden.setCAUSLEGA(objO.get("CAUSLEGA").getAsString());
                                }
                                if (!(objO.get("MOTILEGA").isJsonNull())) {
                                    orden.setMOTILEGA(objO.get("MOTILEGA").getAsString());
                                }
                                if (!(objO.get("CONTCODI").isJsonNull())) {
                                    orden.setCONTCODI(objO.get("CONTCODI").getAsString());
                                }
                                if (!(objO.get("CONTNOMB").isJsonNull())) {
                                    orden.setCONTNOMB(objO.get("CONTNOMB").getAsString());
                                }
                                if (!(objO.get("CONTCICL").isJsonNull())) {
                                    orden.setCONCCICL(objO.get("CONTCICL").getAsString());
                                }
                                if (!(objO.get("CONTDIRE").isJsonNull())) {
                                    orden.setCONTDIRE(objO.get("CONTDIRE").getAsString());
                                }
                                if (!(objO.get("CONTBARR").isJsonNull())) {
                                    orden.setCONTBARR(objO.get("CONTBARR").getAsString());
                                }
                                if (!(objO.get("CONTTEFI").isJsonNull())) {
                                    orden.setCONTTEFI(objO.get("CONTTEFI").getAsString());
                                }
                                if (!(objO.get("CONTTECE").isJsonNull())) {
                                    orden.setCONTTECE(objO.get("CONTTECE").getAsString());
                                }
                                if (!(objO.get("CONTRULE").isJsonNull())) {
                                    orden.setCONTRULE(objO.get("CONTRULE").getAsString());
                                }
                                if (!(objO.get("VALOBARR").isJsonNull())) {
                                    orden.setVALOBARR(objO.get("VALOBARR").getAsString());
                                }
                                if (!(objO.get("DANOPURE").isJsonNull())) {
                                    orden.setDANOPURE(objO.get("DANOPURE").getAsString());
                                }
                                if (!(objO.get("DANOLATI").isJsonNull())) {
                                    orden.setDANOLATI(objO.get("DANOLATI").getAsString());
                                }
                                if (!(objO.get("DANOLONG").isJsonNull())) {
                                    orden.setDANOLONG(objO.get("DANOLONG").getAsString());
                                }
                                if (!(objO.get("DANOALTI").isJsonNull())) {
                                    orden.setDANOALTI(objO.get("DANOALTI").getAsString());
                                }
                                orden.setDescargado(globals.getUsuario_dominio());
                                orden.setORDEEFEC("");
                                orden.setESTADO("Pendiente");
                                cant++;
                                listaOrdens.add(orden);
                                //ordenDao.insert(orden);
                            }
                        }
                    }
                    //Guarda la lista en la base de datos
                    ordenDao.insertInTx(listaOrdens);
                    if(cant>0){
                        showAlertDialog(this, "Orden", "Se han descargado "+String.valueOf(cant)+" ordenes",false);
                    }else{
                        showAlertDialog(this, "Orden", "No existen ordenes nuevas",false);
                    }
                }
            } catch (Exception e) {
                showAlertDialog(this, "Error", e.getMessage(), false);
            }
        }else{
            showAlertDialog(this, "Orden", "No hay registros disponibles para descargar",false);
        }

        this.listView.setAdapter(null);

        buscarOrdenes();
        if (this.pd != null) {
            this.pd.dismiss();
        }

    }

    //Metodo que guarda los datos del pelsonal traidos del servidor
    public void cargarPersona(String datos){
        if(datos.length()>2){
            try{
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                //Valida que existan datos
                if(!array.isJsonNull()) {
                    DAOApp daoApp=new DAOApp();
                    PersonaDao personaDao=daoApp.getPersonaDao();
                    //personaDao.deleteAll();
                    GMA_PKIDDao gma_pkidDao=daoApp.getGma_pkidDao();
                    List<GMA_PKID> gma_pkids=gma_pkidDao.queryBuilder().where(GMA_PKIDDao.Properties.Tabla.in("PersonaOrden")).list();
                    int xx=gma_pkids.get(0).getCantidad();
                    gma_pkids.get(0).setCantidad(xx+array.size());
                    gma_pkidDao.update(gma_pkids.get(0));
                    List<Persona> listaPersonas=new ArrayList<Persona>();
                    //Ciclo que va guardando cada registro en la lista
                    for (int x = 0; x < array.size(); x++) {
                        Persona persona=new Persona();
                        JsonObject objO = array.get(x).getAsJsonObject();
                        if (!(objO.get("perscons").isJsonNull())) {
                            List<Persona> personas=personaDao.queryBuilder().where(PersonaDao.Properties.Perscons.in(objO.get("perscons").getAsInt())).list();
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
                                }
                                listaPersonas.add(persona);
                                //personaDao.insert(persona);
                            }
                        }
                    }
                    //Guarda la lista en la base de datos
                    personaDao.insertInTx(listaPersonas);
                }
            }catch (Exception e){
                showAlertDialog(this, "Personas", e.toString(),false);
            }
        }else{
            //showAlertDialog(this, "Orden", "No hay registros disponibles para descargar "+datos,false);
        }

        if(preparar==0){
            if (this.pd != null) {
                this.pd.dismiss();
            }
            showAlertDialog(this, "Orden", "Personas descargadas.",true);
        }else{
            descargarMateriales();
        }


    }

    //Metodo que guarda los datos del material que vienen del servidor
    public void cargarMaterial(String datos){
        if(datos.length()>2){
            try {
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array=(JsonArray)obje;
                //Valida que existan datos
                if(!array.isJsonNull()) {
                    DAOApp daoApp=new DAOApp();
                    MaterialDao materialDao=daoApp.getMaterialDao();
                    //materialDao.deleteAll();
                    GMA_PKIDDao gma_pkidDao=daoApp.getGma_pkidDao();
                    List<GMA_PKID> gma_pkids=gma_pkidDao.queryBuilder().where(GMA_PKIDDao.Properties.Tabla.in("MaterialOrden")).list();
                    int xx=gma_pkids.get(0).getCantidad();
                    gma_pkids.get(0).setCantidad(xx+array.size());
                    gma_pkidDao.update(gma_pkids.get(0));
                    List<Material> listaMaterials=new ArrayList<Material>();
                    //Ciclo que va guardando cada registro en la lista
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
                                listaMaterials.add(material);
                                //materialDao.insert(material);

                            }
                        }
                    }
                    //Guarda la lista en la base de datos
                    materialDao.insertInTx(listaMaterials);
                }
            } catch (Exception e) {
                showAlertDialog(this, "Material", e.toString(), false);
            }
        }else{
            //showAlertDialog(this, "Orden", "No hay registros disponibles para descargar ",false);
        }

        if(preparar==0){
            if (this.pd != null) {
                this.pd.dismiss();
            }
            showAlertDialog(this, "Orden", "Materiales descargados",true);
        }else{
            descargarCausales();
        }


    }

    //Metodo para mostrar los mensajes de alerta
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

    //Metodo para filtar las ordenes
    public void filtrarOrden(){
        String orden= porOrden.getText().toString();
        String estado= porEstado.getSelectedItem().toString();
        String contrato= porContrato.getText().toString();
        String numeroD= porNumeroD.getText().toString();
        String barrio= porBarrio.getText().toString();
        String ruta= porRuta.getText().toString();
        String efectivida =efectividad.getSelectedItem().toString();
        String tipoD= porTipoD.getSelectedItem().toString();
        String tipoT= porTipoT.getText().toString();

        //Valida que exista al menos una opción seleccionada para filtrar la orden
        if(orden.length()>0||!estado.equals("Por Estado")||contrato.length()>0||numeroD.length()>0||barrio.length()>0||ruta.length()>0||
                !efectivida.equals("Por Efectividad")||!tipoD.equals("Por Tipo de Documento")||tipoT.length()>0){
            listaOrden=ordenDao.loadAll();

            //Filtrar por numero de orden
            if (orden.length()>0){
                List<Gro_orden> ordens=new ArrayList<Gro_orden>();
                for (int x1=0;x1<listaOrden.size();x1++){
                    if (listaOrden.get(x1).getORDECONS().equals(orden)){
                        ordens.add(listaOrden.get(x1));
                    }
                }
                listaOrden=ordens;
            }

            //Filtrar por Estado
            if (!estado.equals("Por Estado")){
                List<Gro_orden> ordens=new ArrayList<Gro_orden>();
                for (int x1=0;x1<listaOrden.size();x1++){
                    if (listaOrden.get(x1).getESTADO().equals(estado)){
                        ordens.add(listaOrden.get(x1));
                    }
                }
                listaOrden=ordens;
            }

            //Filtrar por Contrato
            if (contrato.length()>0){
                List<Gro_orden> ordens=new ArrayList<Gro_orden>();
                for (int x1=0;x1<listaOrden.size();x1++){
                    if (listaOrden.get(x1).getCONTCODI().equals(contrato)){
                        ordens.add(listaOrden.get(x1));
                    }
                }
                listaOrden=ordens;
            }

            //Filtrar por tipo de Orden
            if (numeroD.length()>0){
                List<Gro_orden> ordens=new ArrayList<Gro_orden>();
                for (int x1=0;x1<listaOrden.size();x1++){
                    if (listaOrden.get(x1).getORDENUDO().equals(numeroD)){
                        ordens.add(listaOrden.get(x1));
                    }
                }
                listaOrden=ordens;
            }

            //Filtrar por Barrio
            if (barrio.length()>0){
                List<Gro_orden> ordens=new ArrayList<Gro_orden>();
                for (int x1=0;x1<listaOrden.size();x1++){
                    if (listaOrden.get(x1).getVALOBARR().equals(barrio)){
                        ordens.add(listaOrden.get(x1));
                    }
                }
                listaOrden=ordens;
            }

            //Filtrar por ruta
            if (ruta.length()>0){
                List<Gro_orden> ordens=new ArrayList<Gro_orden>();
                for (int x1=0;x1<listaOrden.size();x1++){
                    if (listaOrden.get(x1).getCONTRULE().equals(ruta)){
                        ordens.add(listaOrden.get(x1));
                    }
                }
                listaOrden=ordens;
            }

            //Filtrar por efectividad
            if (!efectivida.equals("Por Efectividad")){
                List<Gro_orden> ordens=new ArrayList<Gro_orden>();
                for (int x1=0;x1<listaOrden.size();x1++){
                    if (listaOrden.get(x1).getORDEEFEC().equals(efectivida)){
                        ordens.add(listaOrden.get(x1));
                    }
                }
                listaOrden=ordens;
            }

            //Filtrar por tipo de documento
            if (!tipoD.equals("Por Tipo de Documento")){
                List<Gro_orden> ordens=new ArrayList<Gro_orden>();
                for (int x1=0;x1<listaOrden.size();x1++){
                    if (listaOrden.get(x1).getTIPODOCU().equals(tipoD)){
                        ordens.add(listaOrden.get(x1));
                    }
                }
                listaOrden=ordens;
            }

            //Filtrar por tipo de trabajo
            if (tipoT.length()>0){
                List<Gro_orden> ordens=new ArrayList<Gro_orden>();
                for (int x1=0;x1<listaOrden.size();x1++){
                    if (listaOrden.get(x1).getORDETITR().equals(tipoT)){
                        ordens.add(listaOrden.get(x1));
                    }
                }
                listaOrden=ordens;
            }

            //Limpia formulario de filtrar
            if(listaOrden.size()>0){
                this.listView.setAdapter(new OrdenAdapter(this, listaOrden));
                showAlertDialog(this, "Orden", "Ordenes filtradas exitosamente",false);
                formularioFiltrar.setVisibility(View.GONE);
                porEstado.setSelection(0);
                efectividad.setSelection(0);
                porTipoD.setSelection(0);
                porTipoT.setText("");
                porBarrio.setText("");
                porContrato.setText("");
                porOrden.setText("");
                porRuta.setText("");
                porNumeroD.setText("");
                controlLista=true;
            }else{
                this.listView.setAdapter(new OrdenAdapter(this, listaOrden));
                showAlertDialog(this, "Orden", "No hay datos para mostrar",false);
            }

        }else{
            showAlertDialog(this, "Orden", "Filtrar por lo menos en una opción",false);
        }
    }


    //Metodo para ordenar las ordenes
    public void ordenarOrden(String dato){
        List<Gro_orden> aaa=new ArrayList<Gro_orden>();
        try{
            if(dato.equals("orden")){
                aaa = ordenDao.queryBuilder().orderAsc(Gro_ordenDao.Properties.ORDECONS).list();
                //aaa=ordenarCons(aaa);
            }else if(dato.equals("estado")){
                aaa = ordenDao.queryBuilder().orderAsc(Gro_ordenDao.Properties.ESTADO).list();
            }else if(dato.equals("contrato")){
                aaa = ordenDao.queryBuilder().orderAsc(Gro_ordenDao.Properties.CONTCODI).list();
            }else if(dato.equals("tipo de documento")){
                aaa = ordenDao.queryBuilder().orderAsc(Gro_ordenDao.Properties.TIPODOCU).list();
            }else if(dato.equals("numero de documento")){
                aaa = ordenDao.queryBuilder().orderAsc(Gro_ordenDao.Properties.ORDENUDO).list();
            }else if(dato.equals("tipo de trabajo")){
                aaa = ordenDao.queryBuilder().orderAsc(Gro_ordenDao.Properties.ORDETITR).list();
            }else if(dato.equals("barrio")){
                aaa = ordenDao.queryBuilder().orderAsc(Gro_ordenDao.Properties.VALOBARR).list();
            }else if(dato.equals("ruta")){
                aaa = ordenDao.queryBuilder().orderAsc(Gro_ordenDao.Properties.CONTRULE).list();
            }else if(dato.equals("efectividad")){
                aaa = ordenDao.queryBuilder().orderAsc(Gro_ordenDao.Properties.ORDEEFEC).list();
            }

            listaOrden.clear();
            for(int x=0;x<aaa.size();x++){
                if(!aaa.get(x).getESTADO().equals("Cerrada")&&!aaa.get(x).getESTADO().equals("Sincronizada")){
                    listaOrden.add(aaa.get(x));
                }
            }

        }catch (Exception e){
            showAlertDialog(this, "Orden", e.toString(),false);
        }


        if(listaOrden.size()>0){
            this.listView.setAdapter(new OrdenAdapter(this, listaOrden));
            showAlertDialog(this, "Orden", "Ordenes ordenas exitosamente",false);
        }else{
            showAlertDialog(this, "Orden", "No hay datos para mostrar",false);
        }
    }

    //Metodo para guardar los datos del causal traido del servidor
    public void cargarCausal(String datos) {
        if (datos.length() > 2) {
            try {
                JsonParser parser = new JsonParser();
                Object obje = parser.parse(datos);
                JsonArray array = (JsonArray) obje;

                //Validad que existan datos
                if (!array.isJsonNull()) {
                    DAOApp daoApp=new DAOApp();
                    GMA_CAUSALDao causalDao=daoApp.getGMA_CAUSALDao();
                    //causalDao.deleteAll();
                    GMA_PKIDDao gma_pkidDao=daoApp.getGma_pkidDao();
                    List<GMA_PKID> gma_pkids=gma_pkidDao.queryBuilder().where(GMA_PKIDDao.Properties.Tabla.in("CausalOrden")).list();
                    int xx=gma_pkids.get(0).getCantidad();
                    gma_pkids.get(0).setCantidad(xx+array.size());
                    gma_pkidDao.update(gma_pkids.get(0));
                    List<GMA_CAUSAL> listaCausals=new ArrayList<GMA_CAUSAL>();

                    //ciclo que guarda cada registro en la lista
                    for (int x = 0; x < array.size(); x++) {
                        GMA_CAUSAL causal = new GMA_CAUSAL();
                        JsonObject objO = array.get(x).getAsJsonObject();
                        if (!(objO.get("causcons").isJsonNull())) {
                            List<GMA_CAUSAL> causals = causalDao.queryBuilder().where(GMA_CAUSALDao.Properties.Causcons.in(objO.get("causcons").getAsInt())).list();
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
                                listaCausals.add(causal);
                                //causalDao.insert(causal);
                            }
                        }
                    }
                    causalDao.insertInTx(listaCausals);
                }
            } catch (Exception e) {
                showAlertDialog(this, "Causal", e.toString(), false);
            }
        } else {
            showAlertDialog(this, "Orden", "No hay registros disponibles para descargar "+datos, false);
        }
        if(preparar==0){
            showAlertDialog(this, "Orden", "Causales descargadas.", false);
            if (this.pd != null) {
                this.pd.dismiss();
            }
        }else{
            descargarAtributos();
        }


    }
    @Override//Metodo que escucha a cual componente se le ha dado click
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnOrdenOrdenarRegresar:
                formularioOrdenar.setVisibility(View.GONE);
                controlLista=true;
                spOrdenarPor.setSelection(0);
                break;
            case R.id.btnOrdenOrdenarAccion:
                String ordenar=spOrdenarPor.getSelectedItem().toString();
                if (!ordenar.equals("Filtrar por")){
                    ordenarOrden(ordenar);
                    formularioOrdenar.setVisibility(View.GONE);
                    controlLista=true;
                }else{
                    showAlertDialog(this, "Orden", "Seleccionar una opcion para ordenar",false);
                }
                break;
            case R.id.btnOrdenFiltrarRegresar:
                formularioFiltrar.setVisibility(View.GONE);
                controlLista=true;
                break;
            case R.id.btnOrdenFiltrarAccion:
                filtrarOrden();
                break;
            case R.id.btnVerTodas:
                listaOrden=ordenDao.loadAll();
                this.listView.setAdapter(new OrdenAdapter(this, listaOrden));
                formularioFiltrar.setVisibility(View.GONE);
                controlLista=true;
                break;
        }

    }

    //Clase para consumir servicio por get
    private class MiTareaGet extends AsyncTask<String, Float, String> {
        private String ur;

        public MiTareaGet(String url,String x){
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

        @Override//Acción a realizar despues de consumir el servicio
        protected void onPostExecute(String tiraJson) {
            if(control==1){
                cargarDatos(tiraJson);
            }else if(control==2){
                cargarPersona(tiraJson);
            }else if(control==3){
                cargarMaterial(tiraJson);
            }else if(control==4){
                cargarCausal(tiraJson);
            }else if(control==5){
                cargarAtributos(tiraJson);
            }
        }
    }


    //Clase para consumir servicio por post
    private class MiTareaPost extends AsyncTask<String, Float, String> {
        private String jsonObject;
        private final String HTTP_EVENT;
        private HttpClient httpclient;
        BufferedReader in = null;

        public MiTareaPost(String url,String jsonObject){
            Log.d("url",url);
            Log.d("jsonObject",jsonObject);
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

        //Accion a realiazar despues de consumir el servicio
        protected void onPostExecute(String tiraJson) {
            if(control==1){
                cargarDatos(tiraJson);
            }else{
                if(control==5){
                    cargarAtributos(tiraJson);
                }
            }

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
