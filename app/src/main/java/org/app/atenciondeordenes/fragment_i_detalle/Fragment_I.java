package org.app.atenciondeordenes.fragment_i_detalle;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.app.appgenesis.Globals;
import org.app.appgenesis.R;
import org.app.appgenesis.dao.*;
import org.app.appgenesis.dao.Material;
import org.app.appgenesis.dao.Persona;
import org.app.atenciondeordenes.DAOApp;
import org.app.atenciondeordenes.MiServicio;
import org.app.atenciondeordenes.VerificarConexion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class Fragment_I extends Fragment {
    TextView orden;
    TextView estado;
    TextView solicitud;
    TextView fecha;
    TextView unidadOperativa;
    TextView TipoTrabajo;
    TextView contrato;
    TextView persona;
    TextView direccion;
    TextView telefono;
    TextView uso;
    TextView estrato;
    TextView medidor;
    TextView ruta;
    TextView latitud;
    TextView longitud;
    TextView labelSolicitud;
    EditText observacion;
    Spinner efectividad;
    Gro_orden detalleOrden;
    Button btnGuardar;
    Button btnIniciar;
    Button btnSuspender;
    Button btnCerrar;
    int est=0;
    int controlBoton=0;
    private Gro_ordenDao ordenDao;
    private ProgressDialog pd = null;
    double latitu;
    double longitu;
    double altitu;
    String dateText;
    String cel;
    AutoCompleteTextView acCausal;
    View rootView;
    private Globals globals = Globals.getInstance();

    public Fragment_I() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment__i, container, false);
        orden=(TextView)rootView.findViewById(R.id.tvDetalleOrden);
        estado=(TextView)rootView.findViewById(R.id.tvDetalleEstado);
        solicitud=(TextView)rootView.findViewById(R.id.tvDetalleSolicitud);
        fecha=(TextView)rootView.findViewById(R.id.tvDetalleFecha);
        unidadOperativa=(TextView)rootView.findViewById(R.id.tvDetalleUnidadOperativa);
        TipoTrabajo=(TextView)rootView.findViewById(R.id.tvDetalleTipoTrabajo);
        contrato=(TextView)rootView.findViewById(R.id.tvDetalleContrato);
        persona=(TextView)rootView.findViewById(R.id.tvDetallePersona);
        direccion=(TextView)rootView.findViewById(R.id.tvDetalleDireccion);
        telefono=(TextView)rootView.findViewById(R.id.tvDetalleTelefono);
        uso=(TextView)rootView.findViewById(R.id.tvDetalleUso);
        estrato=(TextView)rootView.findViewById(R.id.tvDetalleEstrato);
        medidor=(TextView)rootView.findViewById(R.id.tvDetalleMedidor);
        ruta=(TextView)rootView.findViewById(R.id.tvDetalleRuta);
        latitud=(TextView)rootView.findViewById(R.id.tvDetalleLatitud);
        longitud=(TextView)rootView.findViewById(R.id.tvDetalleLongitud);
        labelSolicitud=(TextView)rootView.findViewById(R.id.tvDetalleLabelSolicitud);
        observacion=(EditText) rootView.findViewById(R.id.etDetalleObservacion);
        efectividad=(Spinner) rootView.findViewById(R.id.etDetalleEfectivida);
        btnGuardar=(Button)rootView.findViewById(R.id.btnGuardarDetalle);
        btnIniciar=(Button)rootView.findViewById(R.id.btnIniciarDetalle);
        btnSuspender=(Button)rootView.findViewById(R.id.btnSuspenderDetalle);
        btnCerrar=(Button)rootView.findViewById(R.id.btnCerrarDetalle);
        acCausal=(AutoCompleteTextView)rootView.findViewById(R.id.acCausal);

        String[] t={"Efectiva","Inefectiva"};
        ArrayAdapter<String> spFormatoAdaptener=new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_spinner_item,t);
        efectividad.setAdapter(spFormatoAdaptener);
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarOrden();
            }
        });
        btnSuspender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suspenderOrden();
            }
        });
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarOrden();
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarOrden();
            }
        });
        long ordenId= getArguments().getLong("id");
        DAOApp d=new DAOApp();
        ordenDao=d.getGro_ordenDao();
        GMA_CAUSALDao causalDao=d.getGMA_CAUSALDao();
        List<GMA_CAUSAL> causals=new ArrayList<GMA_CAUSAL>();
        causals=causalDao.loadAll();

        //Cargar el AutoCompleteTextView
        if (causals.size()>0){
            String[] auto=new String[causals.size()];
            for (int i=0;i<causals.size();i++){
                auto[i]=causals.get(i).getCausdesc();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(),
                    android.R.layout.simple_dropdown_item_1line, auto);
            acCausal.setAdapter(adapter);
        }

        buscarDetalle(ordenId);

        Log.d("Framento:", "FragmentI_load");

        return rootView;
    }

    /*
        //Verficicar si tiene conexion wifi o datos
        boolean conexion=new VerificarConexion(rootView.getContext()).estaConectado();
        if(conexion){

        }else{
            showAlertDialog(rootView.getContext(), "Error", "No tienes Conexión en este momento",false);
        }
    */

    //Metodo Para Guardar la orden
    public void  guardarOrden(){
        //Valida que la orden no este cerrada (est==3 equivale a que la orden esta cerrada)
        if (est==3){
            showAlertDialog(rootView.getContext(), "Error", "Esta orden no se puede editar porque esta CERRADA",false);
        }else{
            //Valida que la orden este iniciada (est==1 equivale a que la orden esta abierta
            if (est==1){
                String efec=efectividad.getSelectedItem().toString();
                String observ= observacion.getText().toString();
                //Valida que el usuario especifique que la orden sea "Efectiva" o "No efectiva"
                if(efec.length()>0 ){
                    detalleOrden.setORDEEFEC(efec);
                    detalleOrden.setORDEOBSE(observ);
                    String causal=acCausal.getText().toString();
                    //Valida que el usuario especifique causal
                    if(causal.length()>0){
                        DAOApp d=new DAOApp();;
                        GMA_CAUSALDao causalDao=d.getGMA_CAUSALDao();
                        //Valida que lo incluido en EditText Causal contenga una opcion Correcta
                        List<GMA_CAUSAL> causals=causalDao.queryBuilder().where(GMA_CAUSALDao.Properties.Causdesc.in(causal)).list();
                        if (causals.size()>0){
                            detalleOrden.setORDECAUS(causal);
                            ordenDao.update(detalleOrden);
                            showAlertDialog(rootView.getContext(), "Orden", "Datos Guardados exitosamente",false);
                        }else{
                            showAlertDialog(rootView.getContext(), "Orden", "Este causal no esta en la base de datos",false);
                        }
                    }else{
                        ordenDao.update(detalleOrden);
                        showAlertDialog(rootView.getContext(), "Orden", "Datos Guardados exitosamente",false);
                    }
                }else{
                    showAlertDialog(rootView.getContext(), "Error", "Debe especificar el campo Efectividad",false);
                }
            }else {
                showAlertDialog(rootView.getContext(), "Orden", "Para editar esta orden primero debe estar iniciada",false);
            }
        }
    }

    //Metodo para cerrar la orden
    public void cerrarOrden(){
            //Valida que la orden no este cerrada
            if (est!=3) {
                //valida que la pestaña detalle este diligenciada al menos una vez
                if (detalleOrden.getORDEEFEC().equals("Efectiva") || detalleOrden.getORDEEFEC().equals("Inefectiva")) {
                    DAOApp daoApp = new DAOApp();
                    Gop_ordeatriDao gop_ordeatriDao = daoApp.getGopOrdeatriDao();
                    long ordenId = getArguments().getLong("id");
                    //Valida que la pestaña atributos este diligenciada al menos una vez
                    List<Gop_ordeatri> gop_ordeatris = gop_ordeatriDao._queryGro_orden_Atributo(ordenId);
                    if (gop_ordeatris.size() > 0) {
                        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(rootView.getContext());
                        dialogo1.setTitle("Importante!");
                        dialogo1.setMessage("¿Desea cerrar esta orden?");
                        dialogo1.setCancelable(false);
                        dialogo1.setPositiveButton("Acceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                pd = ProgressDialog.show(rootView.getContext(), "Procesando", "Espere unos segundos...", true, false);
                                controlBoton = 3;
                                long ordenId = getArguments().getLong("id");

                                JSONObject jsonObject = new JSONObject();
                                try {

                                    JSONArray chilArray = new JSONArray();
                                    JSONObject persObject = new JSONObject();

                                /* Personal de la orden */
                                    JSONArray dataPers = new JSONArray();
                                    DAOApp daoApp = new DAOApp();
                                    Gro_ordenDao gro_ordenDao = daoApp.getGro_ordenDao();
                                    Gro_orden gro_orden = gro_ordenDao.load(ordenId);
                                    PersonaDao personaDao = daoApp.getPersonaDao();
                                    OrdePersDao ordePersDao = daoApp.getOrdePersDao();
                                    List<OrdePers> ordePerses = ordePersDao._queryGro_orden_OrdePers(ordenId);
                                    if (!ordePerses.isEmpty()) {
                                        for (int x = 0; x < ordePerses.size(); x++) {
                                            Persona persona = personaDao.load(ordePerses.get(x).getIdPersona());
                                            JSONObject dataPersObject = new JSONObject();
                                            dataPersObject.put("orpeorde", gro_orden.getORDECONS());
                                            dataPersObject.put("orpepers", persona.getPerscons());
                                            JSONObject esta = new JSONObject();
                                            esta.put("id",2);
                                            dataPersObject.put("orpeesta", esta);
                                            dataPersObject.put("orpeuscr", globals.getUsuario_dominio());
                                            dataPersObject.put("orpefecr", persona.getPersfecr());
                                            dataPersObject.put("orpetipo", "P");
                                            dataPers.put(dataPersObject);
                                        }

                                        persObject.put("tipo", "ORACLE");
                                        persObject.put("tabla", "gopordepers");
                                        persObject.put("data", dataPers);
                                        chilArray.put(persObject);
                                    }

                        /* Materiales de la Orden */
                                    JSONArray dataMate = new JSONArray();
                                    MaterialDao materialDao = daoApp.getMaterialDao();
                                    OrdeMateDao ordeMateDao = daoApp.getOrdeMateDao();
                                    List<OrdeMate> ordeMates = ordeMateDao._queryGro_orden_OrdeMate(ordenId);
                                    if (!ordeMates.isEmpty()) {
                                        for (int x = 0; x < ordeMates.size(); x++) {
                                            Material material = materialDao.load(ordeMates.get(x).getIdMaterial());
                                            JSONObject dataMateObject = new JSONObject();
                                            dataMateObject.put("ormaorde", gro_orden.getORDECONS());
                                            dataMateObject.put("ormamate", material.getMatecons());
                                            dataMateObject.put("ormacant", ordeMates.get(x).getCantidad());
                                            dataMateObject.put("ormavalo", material.getMatecost());
                                            JSONObject esta = new JSONObject();
                                            esta.put("id",2);
                                            dataMateObject.put("ormaesta", esta);
                                            dataMateObject.put("ormauscr", globals.getUsuario_dominio());
                                            dataMateObject.put("ormafecr", material.getMatefecr());
                                            dataMate.put(dataMateObject);
                                        }
                                        JSONObject mateObject = new JSONObject();
                                        mateObject.put("tipo", "ORACLE");
                                        mateObject.put("tabla", "gopordemate");
                                        mateObject.put("data", dataMate);
                                        chilArray.put(mateObject);
                                    }


                        /* Atributos de la orden */
                                    JSONArray dataAtri = new JSONArray();
                                    Gop_ordeatriDao gop_ordeatriDao = daoApp.getGopOrdeatriDao();
                                    List<Gop_ordeatri> gop_ordeatris = gop_ordeatriDao._queryGro_orden_Atributo(ordenId);
                                    if (!gop_ordeatris.isEmpty()) {

                                        for (int x = 0; x < gop_ordeatris.size(); x++) {
                                            Gop_ordeatri gop_ordeatri = gop_ordeatris.get(x);
                                            if (gop_ordeatri.getValor() != null) {
                                                JSONObject dataAtriObject = new JSONObject();
                                                dataAtriObject.put("oratorde", gro_orden.getORDECONS());
                                                dataAtriObject.put("oratatri", gop_ordeatri.getAtricons());
                                                dataAtriObject.put("oratvalo", gop_ordeatri.getRespuesta());
                                                dataAtriObject.put("oratorig", 2); //siempre 2
                                                JSONObject esta = new JSONObject();
                                                esta.put("id",2);
                                                dataAtriObject.put("oratesta", esta); //siempre 2
                                                dataAtriObject.put("oratuscr", globals.getUsuario_dominio()); //Esto debe venir desde la entidad
                                                dataAtriObject.put("oratfecr", gop_ordeatri.getCreacion()); //Esto debe venir desde la entidad
                                                dataAtri.put(dataAtriObject);
                                            }
                                        }

                                        JSONObject atriObject = new JSONObject();
                                        atriObject.put("tipo", "ORACLE");
                                        atriObject.put("tabla", "gopordeatri");
                                        atriObject.put("data", dataAtri);
                                        chilArray.put(atriObject);
                                    }

                                /* Comentarios de la Orden */
                                    JSONArray datacome = new JSONArray();
                                    ComentarioDao comentarioDao = daoApp.getComentarioDao();
                                    List<Comentario> comentarios = comentarioDao._queryGro_orden_Comentario(ordenId);
                                    if (!comentarios.isEmpty()) {
                                        for (int x = 0; x < comentarios.size(); x++) {
                                            Comentario comentario = comentarios.get(x);
                                            JSONObject dataComeObject = new JSONObject();
                                            dataComeObject.put("comedesc", comentario.getComedesc());
                                            dataComeObject.put("comeorde", gro_orden.getORDECONS());
                                            JSONObject esta = new JSONObject();
                                            esta.put("id",2);
                                            dataComeObject.put("comeesta", esta); //siempre 2
                                            dataComeObject.put("comeuscr", globals.getUsuario_dominio()); //Esto debe venir desde la entidad
                                            dataComeObject.put("comefecr", comentario.getComefecr()); //Esto debe venir desde la entidad
                                            datacome.put(dataComeObject);
                                        }

                                        JSONObject comeObject = new JSONObject();
                                        comeObject.put("tipo", "ORACLE");
                                        comeObject.put("tabla", "gmacomentario");
                                        comeObject.put("data", datacome);
                                        chilArray.put(comeObject);
                                    }
                                /* fotografia de la Orden */
                                    JSONArray datafoto = new JSONArray();
                                    FotografiaDao fotografiaDao = daoApp.getFotografiaDao();
                                    List<Fotografia> fotografias = fotografiaDao._queryGro_orden_Fotografia(ordenId);
                                    if (!fotografias.isEmpty()) {
                                        for (int x = 0; x < fotografias.size(); x++) {
                                            Fotografia fotografia = fotografias.get(x);
                                            JSONObject dataComeObject = new JSONObject();
                                            dataComeObject.put("tipodocumento", "orden");
                                            dataComeObject.put("numerodocumento", detalleOrden.getORDECONS());
                                            dataComeObject.put("origen", 2); //siempre 2
                                            dataComeObject.put("representacion", "fotografia");
                                            dataComeObject.put("tipoarchivo", "imagen");
                                            dataComeObject.put("basearchivo", fotografia.getFoto().replaceAll("\n", ""));
                                            dataComeObject.put("extensionarchivo", "JPEG");
                                            dataComeObject.put("estadoarchivo", 2);
                                            dataComeObject.put("usuariocreacion", globals.getUsuario_dominio());
                                            dataComeObject.put("fechacreacion", fotografia.getFecha());
                                            datafoto.put(dataComeObject);
                                        }

                                   /* JSONObject comeObject=new JSONObject();
                                    comeObject.put("tipo","MONGODB");
                                    comeObject.put("medios","gmacomentario");
                                    comeObject.put("data",datafoto);
                                    chilArray.put(comeObject);*/
                                    }

                                /* firmade la Orden */
                                    JSONArray datafirma = new JSONArray();
                                    FirmaDao firmaDao = daoApp.getFirmaDao();
                                    List<Firma> firmas = firmaDao._queryGro_orden_Firma(ordenId);
                                    if (!firmas.isEmpty()) {
                                        for (int x = 0; x < firmas.size(); x++) {
                                            Firma firma = firmas.get(x);
                                            JSONObject dataComeObject = new JSONObject();
                                            dataComeObject.put("tipodocumento", "orden");
                                            dataComeObject.put("numerodocumento", detalleOrden.getORDECONS());
                                            dataComeObject.put("origen", 2); //siempre 2
                                            dataComeObject.put("representacion", "firma");
                                            dataComeObject.put("tipoarchivo", "imagen");
                                            dataComeObject.put("basearchivo", firma.getFoto().replaceAll("\n", ""));
                                            dataComeObject.put("extensionarchivo", "JPEG");
                                            dataComeObject.put("estadoarchivo", 2);
                                            dataComeObject.put("usuariocreacion", globals.getUsuario_dominio());
                                            dataComeObject.put("fechacreacion", firma.getFecha());
                                            datafoto.put(dataComeObject);
                                        }

                                        JSONObject comeObject = new JSONObject();
                                        comeObject.put("tipo", "MONGODB");
                                        comeObject.put("tabla", "medios");
                                        comeObject.put("data", datafoto);
                                        chilArray.put(comeObject);
                                    }

                        /* Detalle de la orden de trabajo */

                                    JSONArray jsonArray = new JSONArray();
                                    JSONObject ordedetalleObject = new JSONObject();
                                    ordedetalleObject.put("ordecons", detalleOrden.getORDECONS());
                                    ordedetalleObject.put("ordefiej", detalleOrden.getORDEFIEJ());

                                    Calendar fech = new GregorianCalendar();
                                    Date date = new Date(fech.getTimeInMillis());
                                    SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    detalleOrden.setORDEFFEJ(df2.format(date));

                                    ordedetalleObject.put("ordeffej", df2.format(date));
                                    ordedetalleObject.put("ordevalu", detalleOrden.getORDEVALU());
                                    ordedetalleObject.put("ordeobse", detalleOrden.getORDEOBSE());
                                    ordedetalleObject.put("ordeefec", detalleOrden.getORDEEFEC());
                                    TelephonyManager telephonyManager = (TelephonyManager) rootView.getContext().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                                    cel = telephonyManager.getDeviceId();
                                    ordedetalleObject.put("ordeterm", cel); //Esto debe ser capturado del telefono
                                    ordedetalleObject.put("ordelati", detalleOrden.getORDELATI());
                                    ordedetalleObject.put("ordelong", detalleOrden.getORDELONG());
                                    ordedetalleObject.put("ordealti", detalleOrden.getORDEALTI());
                                    JSONObject esta = new JSONObject();
                                    esta.put("id",2);
                                    ordedetalleObject.put("ordeesta", esta);
                                    List<Ordecost> ordecost = new ArrayList<Ordecost>();
                                    OrdecostDao ordecostDao = daoApp.getOrdecostDao();
                                    ordecost = ordecostDao._queryGro_orden_Ordecost(ordenId);
                                    if(ordecost.size()>0){
                                        ordedetalleObject.put("costresp", ordecost.get(0).getUsua());
                                        ordedetalleObject.put("costusua", ordecost.get(0).getSscr());
                                        ordedetalleObject.put("costcont", ordecost.get(0).getResp());
                                    }else{
                                        ordedetalleObject.put("costresp", null);
                                        ordedetalleObject.put("costusua", null);
                                        ordedetalleObject.put("costcont", null);
                                    }
                                    ordedetalleObject.put("childs", chilArray);

                                    JSONObject ordeObject = new JSONObject();
                                    ordeObject.put("tipo", "ORACLE");
                                    ordeObject.put("data", ordedetalleObject);

                                    jsonArray.put(ordeObject);

                                    JSONObject syncObject = new JSONObject();
                                    syncObject.put("microservicio", "Gendanos");
                                    syncObject.put("tabla", "goporden");
                                    syncObject.put("operacion", "insertSinc");
                                    syncObject.put("info", jsonArray);

                                    jsonObject.put("sync_ordenes", syncObject);

                                    cerrarOrdenLocalmente();

                                //Verficicar si tiene conexion wifi o datos
                                    boolean conexion = new VerificarConexion(rootView.getContext()).estaConectado();
                                    if (conexion) {
                                        // Log.d("JSON", jsonObject.toString());
                                        new MiTareaPersonal("https://appexp.akc.co/akc/util/genUtilitario/util/sync", jsonObject.toString()).execute();
                                    } else {
                                        showAlertDialog(rootView.getContext(), "Exito", "Orden Cerrada Exitosamete", false);
                                        if (pd != null) {
                                            pd.dismiss();
                                        }
                                    }

                                } catch (Exception e) {
                                    Log.d("Error capturado ::: ", "-" + e.getMessage());
                                    e.printStackTrace();
                                }
                            }
                        });
                        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {

                            }
                        });
                        dialogo1.show();

                    } else {
                        showAlertDialog(rootView.getContext(), "Orden", "Antes de cerrar debe diligenciar pestaña atributo", false);
                    }

                } else {
                    showAlertDialog(rootView.getContext(), "Orden", "Antes de cerrar debe diligenciar pestaña detalle", false);
                }
            }
        //Valida que la orden no este cerrada

    }

    //Metodo para suspender la orden
    public void suspenderOrden(){
        //validad que la orden no este suspendiada
        if(est!=2){
            //valida que la orden este iniciada
            if (detalleOrden.getESTADO().equals("Iniciada")){
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(rootView.getContext());
                dialogo1.setTitle("Importante!");
                dialogo1.setMessage("¿Desea suspender esta orden?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Acceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        pd = ProgressDialog.show(rootView.getContext(), "Procesando", "Espere unos segundos...", true, false);
                        controlBoton=2;

                        try {
                            Calendar fech = new GregorianCalendar();
                            Date date=new Date(fech.getTimeInMillis());
                            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            dateText = df2.format(date);
                            DAOApp daoApp=new DAOApp();
                            Gro_ordenDao gro_ordenDao=daoApp.getGro_ordenDao();
                            long ordenId= getArguments().getLong("id");
                            Gro_orden gro_orden= gro_ordenDao.load(ordenId);
                            supenderOrdenLocalmente();
                            //Verficicar si tiene conexion wifi o datos
                            boolean conexion=new VerificarConexion(rootView.getContext()).estaConectado();
                            if(conexion){
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("ordefecr",dateText);
                                jsonObject.put("ordecons",Integer.valueOf(gro_orden.getORDECONS()));
                                jsonObject.put("ordeusua",globals.getUsuario_dominio());
                                //detalleOrden.setESTADO("Suspendida");
                                new MiTareaPersonal(globals.getServer()+"/Gendanos/goporden/suspenderOrden",jsonObject.toString()).execute();
                            }else{
                                showAlertDialog(rootView.getContext(), "Exito","Orden Suspendida Localmente",false);
                                if (pd != null) {
                                    pd.dismiss();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                dialogo1.show();
            }else {
                //valida si la orden esta cerrada o no iniciada
                if(!detalleOrden.getESTADO().equals("Cerrada")){
                    showAlertDialog(rootView.getContext(), "Orden", "Para suspender esta orden primero debe estar iniciada",false);
                }else{
                    showAlertDialog(rootView.getContext(), "Error", "Esta orden no se puede editar porque esta CERRADA",false);
                }
            }
        }
    }

    //Metodo para Iniciar la Orden
    public void iniciarOrden(){
        //Valida que la orden no este iniciada
        if(est!=1){
            //valida que la orden no esta cerrada ni sincronizada
            if(!detalleOrden.getESTADO().equals("Cerrada")&&!detalleOrden.getESTADO().equals("Sincronizada")){
                List<Gro_orden> ordens = ordenDao.queryBuilder().where(Gro_ordenDao.Properties.ESTADO.eq("Iniciada")).list();
                //Valida si hay una orden iniciada
                if (ordens.size()>0){
                    showAlertDialog(rootView.getContext(), "Orden", "Tienes un orden iniciada, debe suspenderla para iniciar otra",false);
                }else {
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(rootView.getContext());
                    dialogo1.setTitle("Importante!");
                    dialogo1.setMessage("¿Desea iniciar esta orden?");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Acceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            pd = ProgressDialog.show(rootView.getContext(), "Proceso", "Iniciando Orden. Espere unos segundos...", true, false);

                            MiServicio miServicio=new MiServicio(getActivity().getApplicationContext());

                            if(miServicio.getLatitud()!=0.0 && miServicio.getLongitud()!=0.0){
                                latitu= miServicio.getLatitud();
                                latitud.setText(String.valueOf(latitu));
                                longitu= miServicio.getLongitud();
                                longitud.setText(String.valueOf(longitu));
                                if(miServicio.getAltitud()!=0.0){
                                    altitu=miServicio.getAltitud();
                                }
                                Calendar fech = new GregorianCalendar();
                                Date date=new Date(fech.getTimeInMillis());
                                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                dateText = df2.format(date);
                                TelephonyManager telephonyManager = (TelephonyManager) rootView.getContext().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                                controlBoton=1;
                                try {
                                    cel=telephonyManager.getDeviceId();
                                    iniciarOrdenLocalmente();
                                    //Verficicar si tiene conexion wifi o datos
                                    boolean conexion=new VerificarConexion(rootView.getContext()).estaConectado();
                                    if(conexion){
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("ordefiej",dateText);
                                        jsonObject.put("ordeterm",cel);
                                        jsonObject.put("ordelati",latitu);
                                        jsonObject.put("ordelong",longitu);
                                        jsonObject.put("ordealti",altitu);
                                        DAOApp daoApp=new DAOApp();
                                        Gro_ordenDao gro_ordenDao=daoApp.getGro_ordenDao();
                                        long ordenId= getArguments().getLong("id");
                                        Gro_orden gro_orden= gro_ordenDao.load(ordenId);
                                        jsonObject.put("ordecons",Integer.valueOf(gro_orden.getORDECONS()));
                                        jsonObject.put("ordeusua", globals.getUsuario_dominio());
                                        new MiTareaPersonal(globals.getServer() + "/Gendanos/goporden/iniciarOrden",jsonObject.toString()).execute();
                                    }else{
                                        showAlertDialog(rootView.getContext(), "Exito","Orden Iniciada Localmente",false);
                                        if (pd != null) {
                                            pd.dismiss();
                                        }
                                    }

                                } catch (JSONException e) {
                                    if (pd != null) {
                                        pd.dismiss();
                                    }
                                    showAlertDialog(rootView.getContext(), "Error", "Ha ocurrido una Excepción: " + e.getMessage(),false);
                                    //e.printStackTrace();
                                }
                            }else {
                                if (pd != null) {
                                    pd.dismiss();
                                }
                                showAlertDialog(rootView.getContext(), "Error", "No se puedo calcular las coordenadas. Verifique que tiene activada Localización",false);
                            }
                        }
                    });

                    dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                        }
                    });
                    dialogo1.show();
                }
            }else{
                showAlertDialog(rootView.getContext(), "Error", "Esta orden no se puede editar porque esta CERRADA",false);
            }
        }
    }

    //Metodo para buscar los consultar los datos de la orden en la base de datos
    public void buscarDetalle(long dato){
        try {
            detalleOrden= this.ordenDao.load(dato);
            //Mostrar los datos de la orden
            cargarDatos(detalleOrden);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Metodo para mostrar los datos de la orden
    public void cargarDatos(Gro_orden detalle){
        long tmp = Long.valueOf(detalle.getORDEFECR());
        Date date=new Date(tmp);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateText = df2.format(date);
        orden.setText(String.valueOf(detalle.getORDECONS()));
        estado.setText(detalle.getESTADO());
        solicitud.setText(detalle.getORDENUDO());
        fecha.setText(dateText);
        unidadOperativa.setText(detalle.getUNOPNOMB());
        TipoTrabajo.setText(detalle.getTITRDESC());
        contrato.setText(detalle.getCONTCODI());
        persona.setText(detalle.getCONTNOMB());
        direccion.setText(detalle.getCONTDIRE()+" "+detalle.getVALOBARR());
        telefono.setText(detalle.getCONTTEFI()+"/"+detalle.getCONTTECE());
        labelSolicitud.setText(detalle.getTIPODOCU());
        uso.setText("");
        estrato.setText("");
        medidor.setText("");
        ruta.setText(detalle.getCONTRULE());
        latitud.setText(detalle.getORDELATI());
        longitud.setText(detalle.getORDELONG());
        observacion.setText(detalle.getORDEOBSE());

        if(detalle.getESTADO().equals("Iniciada")){
            btnIniciar.setBackgroundResource(R.color.verde_3);
            btnSuspender.setBackgroundResource(R.color.verde_2);
            btnCerrar.setBackgroundResource(R.color.verde_2);
            estado.setTextColor(Color.BLUE);
            est=1;
        }else if(detalle.getESTADO().equals("Suspendida")){
            btnSuspender.setBackgroundResource(R.color.verde_3);
            btnIniciar.setBackgroundResource(R.color.verde_2);
            btnCerrar.setBackgroundResource(R.color.verde_2);
            estado.setTextColor(Color.rgb(252,168,0));
            est=2;
        }else if(detalle.getESTADO().equals("Cerrada")){
            btnCerrar.setBackgroundResource(R.color.verde_3);
            btnIniciar.setBackgroundResource(R.color.verde_2);
            btnSuspender.setBackgroundResource(R.color.verde_2);
            estado.setTextColor(Color.GREEN);
            est=3;
        }

        if(detalle.getORDEEFEC().equals("Efectiva")){
            efectividad.setSelection(1);
        }else if(detalle.getORDEEFEC().equals("Inefectiva")){
            efectividad.setSelection(2);
        }else{
            efectividad.setSelection(0);
        }
    }

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

    //Inicar orden localmente
    public void iniciarOrdenLocalmente(){
        detalleOrden.setORDETERM(cel);
        detalleOrden.setORDELONG(String.valueOf(longitu));
        detalleOrden.setORDELATI(String.valueOf(latitu));
        detalleOrden.setORDEALTI(String.valueOf(altitu));
        detalleOrden.setORDEFIEJ(dateText);
        detalleOrden.setESTADO("Iniciada");
        ordenDao.update(detalleOrden);
        GOP_ORDEESTA gop_ordeesta=new GOP_ORDEESTA();
        gop_ordeesta.setORESESTA(detalleOrden.getESTADO());
        gop_ordeesta.setORESFECR(dateText);
        gop_ordeesta.setORESORDE(String.valueOf(detalleOrden.getORDECONS()));
        gop_ordeesta.setORESOBSE(detalleOrden.getORDEOBSE());
        gop_ordeesta.setORESUSCR(globals.getUsuario_dominio());
        DAOApp daoApp=new DAOApp();
        GOP_ORDEESTADao gop_ordeestaDao=daoApp.getGop_ordeestaDao();
        gop_ordeestaDao.insert(gop_ordeesta);
        btnIniciar.setBackgroundResource(R.color.verde_3);
        btnSuspender.setBackgroundResource(R.color.verde_2);
        btnCerrar.setBackgroundResource(R.color.verde_2);
        est = 1;
    }

    //Metodo que recibe respuesta del servidor al iniciar orden
    public void iniciandoOrden(String datos) {
        try {
            JsonParser parser = new JsonParser();
            Object obje = parser.parse(datos);
            JsonObject array=(JsonObject)obje;
            //Valida la respuesta del servidor
            if (!(array.get("respuesta").equals("OK"))){

                showAlertDialog(rootView.getContext(), "Exito", "Orden Iniciada Exitoamente",false);
            }else{
                showAlertDialog(rootView.getContext(), "Exito","Orden Iniciada Localmente",false);
            }
        }catch (Exception e){
            showAlertDialog(rootView.getContext(), "Exito","Orden Iniciada Localmente",false);
        }

        long ordenId= getArguments().getLong("id");
        buscarDetalle(ordenId);
        if (this.pd != null) {
            this.pd.dismiss();
        }
    }

    //Suspender orden localmente
    public void supenderOrdenLocalmente(){
        detalleOrden.setESTADO("Suspendida");
        ordenDao.update(detalleOrden);
        GOP_ORDEESTA gop_ordeesta=new GOP_ORDEESTA();
        gop_ordeesta.setORESESTA(detalleOrden.getESTADO());
        gop_ordeesta.setORESFECR(dateText);
        gop_ordeesta.setORESORDE(String.valueOf(detalleOrden.getORDECONS()));
        gop_ordeesta.setORESOBSE(detalleOrden.getORDEOBSE());
        gop_ordeesta.setORESUSCR(globals.getUsuario_dominio());
        DAOApp daoApp=new DAOApp();
        GOP_ORDEESTADao gop_ordeestaDao=daoApp.getGop_ordeestaDao();
        gop_ordeestaDao.insert(gop_ordeesta);
        btnIniciar.setBackgroundResource(R.color.verde_2);
        btnSuspender.setBackgroundResource(R.color.verde_3);
        btnCerrar.setBackgroundResource(R.color.verde_2);
        est = 2;;
    }

    //Metodo para suspender en la orden luego de consultar al servidor
    public void suspendiendoOrden(String datos) {
        try {
            JsonParser parser = new JsonParser();
            Object obje = parser.parse(datos);
            JsonObject array=(JsonObject)obje;
            //Validar la respuesta del servidor
            if (!(array.get("respuesta").equals("OK"))){

                showAlertDialog(rootView.getContext(), "Exito", "Orden Suspendida Exitosamente",false);
            }else{
                showAlertDialog(rootView.getContext(), "Exito", "Orden Suspendida Localmente",false);
            }
        }catch (Exception e){
            showAlertDialog(rootView.getContext(), "Exito", "Orden Suspendida Localmente",false);
        }
        long ordenId= getArguments().getLong("id");
        buscarDetalle(ordenId);
        if (this.pd != null) {
            this.pd.dismiss();
        }
    }

    //Metodo cerrar orden localmente
    public void cerrarOrdenLocalmente(){
        detalleOrden.setESTADO("Cerrada");
        ordenDao.update(detalleOrden);
        GOP_ORDEESTA gop_ordeesta=new GOP_ORDEESTA();
        gop_ordeesta.setORESESTA(detalleOrden.getESTADO());
        gop_ordeesta.setORESFECR(dateText);
        gop_ordeesta.setORESORDE(String.valueOf(detalleOrden.getORDECONS()));
        gop_ordeesta.setORESOBSE(detalleOrden.getORDEOBSE());
        gop_ordeesta.setORESUSCR(globals.getUsuario_dominio());
        DAOApp daoApp=new DAOApp();
        GOP_ORDEESTADao gop_ordeestaDao=daoApp.getGop_ordeestaDao();
        gop_ordeestaDao.insert(gop_ordeesta);
        btnIniciar.setBackgroundResource(R.color.verde_2);
        btnSuspender.setBackgroundResource(R.color.verde_2);
        btnCerrar.setBackgroundResource(R.color.verde_3);
        est = 3;
    }

    //Metodo para Cerrar la orden luego de consultar al servidor
    public void cerrandoOrden(String datos) {
        try {
            JsonParser parser = new JsonParser();
            Object obje = parser.parse(datos);
            JsonObject array=(JsonObject)obje;
            //Valida la respuesta del servidor
            if (!(array.get("data").equals("true"))){

                showAlertDialog(rootView.getContext(), "Exito", "Orden Cerrada Exitosamente",false);
            }else{
                showAlertDialog(rootView.getContext(), "Exito", "Orden Cerrada Localmente",false);
            }
        }catch (Exception e){
            showAlertDialog(rootView.getContext(), "Exito", "Orden Cerrada Localmente",false);
        }
        long ordenId= getArguments().getLong("id");
        buscarDetalle(ordenId);
        if (this.pd != null) {
            this.pd.dismiss();
        }
    }

    //Clase para generar un hilo en segundo plano para consultar el servidor
    private class MiTareaPersonal extends AsyncTask<String, Float, String> {
        private String jsonObject;
        private final String HTTP_EVENT;
        private HttpClient httpclient;
        BufferedReader in = null;

    public MiTareaPersonal(String url,String jsonObject){

        Log.d("url", url);
        Log.d("JSON",jsonObject);

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
            post.setHeader("Content-type", "application/json");
            post.setHeader("Authorization", globals.getToken_type() + " " + globals.getAccess_token());
            post.setEntity(new StringEntity(jsonObject, HTTP.UTF_8));

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
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resul;
    }

    protected void onProgressUpdate (Float... valores) {

    }
    //Metodo a ejecutar luego de consultar al servidor
    protected void onPostExecute(String tiraJson) {
        if(controlBoton==1){
            iniciandoOrden(tiraJson);
        }else if(controlBoton==2){
            suspendiendoOrden(tiraJson);
        }else if(controlBoton==3){
            cerrandoOrden(tiraJson);
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
            e.printStackTrace();
        }
        return stringBuilder;
    }
}
}
