package org.app.atenciondeordenes.fragment_ii_atributos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.app.appgenesis.Globals;
import org.app.appgenesis.R;
import org.app.appgenesis.dao.GOP_ATRIBUTOS;
import org.app.appgenesis.dao.GOP_ATRIBUTOSDao;
import org.app.appgenesis.dao.Gop_ordeatri;
import org.app.appgenesis.dao.Gop_ordeatriDao;
import org.app.appgenesis.dao.Gro_ordenDao;
import org.app.atenciondeordenes.DAOApp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class Fragment_II extends Fragment {
    private List<Gop_ordeatri> gopOrdeatriList;
    private LinearLayout linearLayout;
    private FormularioAdapter adapter;
    private List<Formulario> formularios;
    private List<List<Formulario>> listFormularios=new ArrayList<>();
    private RecyclerView mRecyclerView;
    private Button btnGuardar,btnDescargar;
    LinearLayoutManager layoutManager;
    private ProgressDialog pd = null;
    private String estado;
    private long ordenId;
    private Gop_ordeatriDao gop_ordeatriDao;
    private Boolean isDatabaseEmpty = false;
    private Globals globals = Globals.getInstance();
    View rootView;
    public Fragment_II() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment__ii, container, false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.rcAtributos);
        layoutManager = new LinearLayoutManager(getActivity());
        linearLayout = (LinearLayout)rootView.findViewById(R.id.linearLayout);
        btnDescargar = (Button)rootView.findViewById(R.id.btnDescargarAtributos);
        btnGuardar = (Button)rootView.findViewById(R.id.btnGuardarAtributos);
        formularios = new ArrayList<>();
        mRecyclerView.setLayoutManager(layoutManager);

        final long titr= getArguments().getLong("titr");

        //Acción Descargar
        btnDescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DAOApp d=new DAOApp();
                Gro_ordenDao ordenDao = d.getGro_ordenDao();
                estado =ordenDao.load(ordenId).getESTADO();
                //Verifica que la orden este iniciada
                if(estado.equals("Iniciada")){
                    //Se verifica si los atributos de la orden ya fueron descargados
                    DAOApp daoApp = new DAOApp();
                    Gop_ordeatriDao gop_ordeatriDao = daoApp.getGopOrdeatriDao();
                    long ordenId = getArguments().getLong("id");
                    List<Gop_ordeatri> gop_ordeatris = gop_ordeatriDao._queryGro_orden_Atributo(ordenId);
                    if (gop_ordeatris.size() > 0) {
                        showAlertDialog(rootView.getContext(), "Orden", "Los datos ya estan descargados",false);
                    }else{
                        //gopOrdeatriList = new ArrayList<>();
                        //pd = ProgressDialog.show(rootView.getContext(), "Proceso", "Descargando Atributos. Espere unos segundos...", true, false);
                        //JSONObject jsonObject = new JSONObject();
                        //try {
                        //jsonObject.put("titrcons",titr);
                        GOP_ATRIBUTOSDao gop_atributosDao = daoApp.getGop_atributosDao();
                        List<GOP_ATRIBUTOS> gop_atributoses = gop_atributosDao.queryBuilder().where(GOP_ATRIBUTOSDao.Properties.Titr.in(titr)).list();

                        if(gop_atributoses==null || gop_atributoses.isEmpty()){
                            alertDialog("No existen atributos para TiTr " + titr);
                        }else{
                            cargarAtributos(gop_atributoses.get(0).getJson());
                        }
                        //new MiTareaPersonal(globals.getServer()+"/Gendanos/goporden/atributosOrden",jsonObject.toString()).execute();
                        // } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    }
                }else {
                    showAlertDialog(rootView.getContext(), "Orden", "Para descargar la orden debe estar iniciada",false);
                }



            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean guardar = true;
                if(formularios.size()==0){
                    alertDialog("No hay datos que guardar");
                    guardar = false;
                }
                for (int i = 0; i <= formularios.size() - 1; i++) {
                    if (formularios.get(i).getAtricons() == gopOrdeatriList.get(i).getAtricons()) {

                        /*esto es nuevo*/
                        String respuestas = "";
                        if(formularios.get(i).getValor()!=null){
                            if(formularios.get(i).getCompAndroid() == FromEnums.CHECK_BOX){

                                if(formularios.get(i)!=null && formularios.get(i).getValor()!=null){
                                    List<String> valor = Arrays.asList(formularios.get(i).getValor().split(","));
                                    Boolean atLeastOne = false;
                                    for (int j = 0; j <= valor.size() - 1; j++) {
                                        if(Boolean.valueOf(valor.get(j))){
                                            atLeastOne = true;
                                            if(!respuestas.isEmpty()){
                                                respuestas += ",";
                                            }
                                            respuestas += gopOrdeatriList.get(i).getValores().split(",")[j];

                                        }
                                    }
                                    if(!atLeastOne){
                                        guardar = false;
                                        alertDialog( "El Campo " + formularios.get(i).getAtridesc() + " es Requerido");
                                        break;
                                    }else{
                                        Log.d("Valor guardado Check:", formularios.get(i).getAtridesc() + " : " + respuestas);
                                        gopOrdeatriList.get(i).setRespuesta(respuestas);
                                        gopOrdeatriList.get(i).setValor(formularios.get(i).getValor().toString());
                                    }
                                }
                            }else{
                                Log.d("Valor guardado:", formularios.get(i).getAtridesc() + " : " +formularios.get(i).getValor().toString() );
                                gopOrdeatriList.get(i).setValor(formularios.get(i).getValor().toString());
                                gopOrdeatriList.get(i).setRespuesta(formularios.get(i).getValor().toString());
                            }
                        }
                        else{
                            Log.d("Valor NULO:", formularios.get(i).getAtridesc());
                            gopOrdeatriList.get(i).setValor(null);
                        }

                        if (formularios.get(i).getRequerido() && formularios.get(i).getValor() == null) {
                            alertDialog("El Campo " + formularios.get(i).getAtridesc() + " es Requerido");
                            guardar = false;
                            break;
                        }
                        /* hasta aqui esto es nuevo*/

                        if(formularios.get(i).getCompAndroid() == FromEnums.CHECK_BOX){

                            if(formularios.get(i)!=null && formularios.get(i).getValor()!=null){
                                List<String> valor = Arrays.asList(formularios.get(i).getValor().split(","));
                                Boolean atLeastOne = false;
                                for (int j = 0; j <= valor.size() - 1; j++) {
                                    if(Boolean.valueOf(valor.get(j))){
                                        atLeastOne = true;
                                    }
                                }
                                if(!atLeastOne){
                                    guardar = false;
                                    alertDialog( "El Campo " + formularios.get(i).getAtridesc() + " es Requerido");
                                    break;
                                }
                            }
                        }
                    }
                    Calendar fech = new GregorianCalendar();
                    Date date=new Date(fech.getTimeInMillis());
                    SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    gopOrdeatriList.get(i).setCreacion(df2.format(date));
                    gopOrdeatriList.get(i).setUsuario(globals.getUsuario_dominio());
                }

                if(guardar){

                    gop_ordeatriDao.updateInTx(gopOrdeatriList);
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                            getActivity());

                    // set title
                    alertDialogBuilder.setTitle("Mensaje");
                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Datos Guardados con exito")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    dialog.cancel();
                                }
                            })
                    ;

                    // create alert dialog
                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                }


            }
        });
        ordenId= getArguments().getLong("id");
        DAOApp d=new DAOApp();
        gop_ordeatriDao=d.getGopOrdeatriDao();
        Gro_ordenDao ordenDao = d.getGro_ordenDao();
        estado =ordenDao.load(ordenId).getESTADO();
        buscarAtributosDB();

        Log.d("Framento:", "FragmentII_load");


        return rootView;
    }

    public void cargarAtributos(String json){
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i=0; i<=jsonArray.length()-1;i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Gop_ordeatri gop_ordeatri = new Gop_ordeatri();
                gop_ordeatri.setAtricons(jsonObject.getLong("ATRICONS"));
                gop_ordeatri.setAtridesc(jsonObject.getString("ATRIDESC"));
                gop_ordeatri.setComp_extjs(jsonObject.getString("COMP_EXTJS"));
                gop_ordeatri.setComp_android(jsonObject.getString("COMP_ANDROID"));
                gop_ordeatri.setGrupo(jsonObject.getString("GRUPO"));
                gop_ordeatri.setRequerido(jsonObject.getString("REQUERIDO"));

                /* Se hace el ajuste de los valores de componentes dinámicos */
                String valores = jsonObject.getString("VALORES");
                if(valores==null || valores.isEmpty()){
                    gop_ordeatri.setValores("");
                }else{
                    String cadena = "";
                    JSONArray json_array = jsonObject.optJSONArray("VALORES");
                    for (int j=0; j<json_array.length(); j++) {

                        cadena += json_array.getJSONObject(j).get("valor").toString() ;
                        if(j+1<json_array.length()){
                            cadena+=",";
                        }
                    }
                    gop_ordeatri.setValores(cadena);
                }

                gop_ordeatri.setIdOrden(ordenId);
                formularios.add(new FormularioImpl(gop_ordeatri));
                gopOrdeatriList.add(gop_ordeatri);
            }
            if(isDatabaseEmpty){
                gop_ordeatriDao.insertInTx(gopOrdeatriList);


            }else{
                gop_ordeatriDao.deleteAll();
                gop_ordeatriDao.insertInTx(gopOrdeatriList);


            }
            buscarAtributosDB();


        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (this.pd != null) {
            this.pd.dismiss();
        }
    }

    private void buscarAtributosDB(){
        //try {

        gopOrdeatriList = gop_ordeatriDao._queryGro_orden_Atributo(ordenId);
        formularios = new ArrayList<>();
        listFormularios = new ArrayList<>();
        for (int i=0; i<=gopOrdeatriList.size()-1;i++){
            formularios.add(new FormularioImpl(gopOrdeatriList.get(i)));
        }

        int numeroMaximoGrupo = 0;
        int numeroGrupo = 0;
        for (int i=0; i<=gopOrdeatriList.size()-1;i++){
            if(numeroMaximoGrupo < Integer.parseInt(formularios.get(i).getGrupo().get(0))){
                numeroMaximoGrupo = Integer.parseInt(formularios.get(i).getGrupo().get(0));
                numeroGrupo = numeroGrupo+1;
                listFormularios.add(new ArrayList<Formulario>());

            }
            listFormularios.get(numeroGrupo-1).add(formularios.get(i));


        }
        if(listFormularios.size() >0){
            adapter = new FormularioAdapter(listFormularios, getActivity());
            adapter.setOnFormularioClickListener(onFormularioClickListener);
            mRecyclerView.setAdapter(adapter);
            isDatabaseEmpty = false;
        }else {
            isDatabaseEmpty = true;
        }

    }
    OnFormularioClickListener onFormularioClickListener = new OnFormularioClickListener() {
        @Override
        public void onItemClicked(Formulario formulario, Integer position) {
            for (int i=0; i<=formularios.size()-1;i++){
                if(formulario.getAtricons() == formularios.get(i).getAtricons()){
                    formularios.set(i,formulario);
                    break;
                }
            }


        }
    };

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

    void alertDialog(String message){

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                getActivity());

        // set title
        alertDialogBuilder.setTitle("Error");
        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                    }
                })
        ;

        // create alert dialog
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private class MiTareaPersonal extends AsyncTask<String, Float, String> {
        private String jsonObject;
        private final String HTTP_EVENT;
        private HttpClient httpclient;
        BufferedReader in = null;


        public MiTareaPersonal(String url,String jsonObject){

            Log.d("url", url);
            Log.d("jsonObject", jsonObject);

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
            cargarAtributos(tiraJson);
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