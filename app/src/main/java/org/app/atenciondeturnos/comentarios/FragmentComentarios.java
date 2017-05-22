package org.app.atenciondeturnos.comentarios;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.app.appgenesis.Globals;
import org.app.appgenesis.R;
import org.app.atenciondeordenes.DAOApp;
import org.app.atenciondeturnos.dao.GDB_TURNOS;
import org.app.atenciondeturnos.dao.GDB_TURNOSDao;
import org.app.atenciondeturnos.dao.TurnCome;
import org.app.atenciondeturnos.dao.TurnComeDao;
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

public class FragmentComentarios extends Fragment {
    private List<TurnCome> listaOrden=new ArrayList<TurnCome>();
    private long turno;
    private EditText etNumero;
    private EditText etComentario;
    private Button btnRegresar;
    private Button btnGuardar;
    private Button btnNuevo;
    private View rootView;
    private RelativeLayout formulario;
    private ListView lvComentario;
    private TurnComeDao comentarioDao;
    private String estado;
    private Button btnDescargar;
    private Globals globals = Globals.getInstance();
    private ProgressDialog pd = null;

    public FragmentComentarios() {
        // Required empty public constructor
    }

    public String getEstado() {
        return estado;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_turno_comentario, container, false);
        etNumero=(EditText)rootView.findViewById(R.id.etNumeroComentarioTurno);
        etComentario=(EditText)rootView.findViewById(R.id.etComentarioTurno);
        btnRegresar=(Button)rootView.findViewById(R.id.btnRegresarComentarioTurno);
        btnGuardar=(Button)rootView.findViewById(R.id.btnGuardarComentarioTurno);
        btnNuevo=(Button)rootView.findViewById(R.id.btnNuevoComentarioTurno);
        formulario=(RelativeLayout) rootView.findViewById(R.id.formularioComentarioTurno);
        lvComentario=(ListView) rootView.findViewById(R.id.lvComentarioMostrarTurno);
        btnDescargar=(Button)rootView.findViewById(R.id.btnDescargarComentarioTurno);
            formulario.setVisibility(View.GONE);
            DAOApp daoApp=new DAOApp();
            comentarioDao=daoApp.getTurnComeDao();
        turno= getArguments().getLong("id");

        GDB_TURNOS turnos= new GDB_TURNOS();
        GDB_TURNOSDao turnosDao= daoApp.getGdb_turnosDao();
        turnos=turnosDao.load(turno);

        //Acción mostrar formulario para guardar nuevo comentario
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulario.setVisibility(View.VISIBLE);
                /*if(!estado.equals("Cerrada")){
                    if(estado.equals("Iniciada")){
                        formulario.setVisibility(View.VISIBLE);
                    }else{
                        showAlertDialog(rootView.getContext(), "Error", "Para realizar esta acción la orden debe estar en estado INICIADA",false);
                    }
                }else{
                    showAlertDialog(rootView.getContext(), "Error", "Esta orden no se puede editar porque esta CERRADA",false);
                }*/

            }
        });
        final GDB_TURNOS finalturno = turnos;

        //Acción para guardar nuevo comentario
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numero=etNumero.getText().toString();
                String comentario=etComentario.getText().toString();
                if(numero.length()>0&&comentario.length()>0){
                    try {
                        TurnCome comentario1= new TurnCome();
                        comentario1.setComedano(String.valueOf(finalturno.getTURNCONS()));
                        comentario1.setComedesc(comentario);
                        comentario1.setIdTurn(turno);
                        comentario1.setComeuscr(globals.getUsuario_dominio());
                        Calendar fech = new GregorianCalendar();
                        Date date=new Date(fech.getTimeInMillis());
                        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        comentario1.setComefecr(df2.format(date));
                        comentarioDao.insert(comentario1);
                        showAlertDialog(rootView.getContext(), "Comentario", "Registro guardado",false);
                        buscarMaterial();
                        formulario.setVisibility(View.GONE);
                        etComentario.setText("");
                    }catch (Exception e){
                        showAlertDialog(rootView.getContext(), "Error", e.toString(),false);
                    }

                }else{
                    showAlertDialog(rootView.getContext(), "Error", "Debe especificar el tipo, numero y Comentario",false);
                }
            }
        });

        //Acción salir del formulario nuevo comentario
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulario.setVisibility(View.GONE);
                //etNumero.setText("");
                etComentario.setText("");
            }
        });

        //Acción Descargar comentarios
        btnDescargar.setVisibility(View.INVISIBLE);
        btnDescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = ProgressDialog.show(rootView.getContext(), "Procesando", "Espere unos segundos...", true, false);
                JSONObject jsonObject = new JSONObject();
                /*try {
                    DAOApp dao=new DAOApp();
                    GDB_TURNOSDao gro_ordenDao=dao.getGdb_turnosDao();
                    GDB_TURNOS gro_turno=gro_ordenDao.load(turno);
                    jsonObject.put("ordenudo",gro_orden.getORDENUDO());
                    jsonObject.put("ordetido",gro_orden.getTIPODOCU());
                    new MiTareaPersonal(globals.getServer()+"/GenadminOp/gmacomentario/findByType",jsonObject.toString()).execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        });

        //estado =turnos.getESTADO();
        etNumero.setText("Turno #"+String.valueOf(turnos.getTURNCONS()));
        buscarMaterial();
        return rootView;
    }

    //Metodo para cargar listado de materiales
    public void buscarMaterial(){
        try {
            listaOrden=comentarioDao._queryGDB_TURNOS_Comentario(turno);

            //Llamar adapter para mostrar el listado de materiales en la vista
            this.lvComentario.setAdapter(new TurnoComentarioAdapter(rootView.getContext(), listaOrden,FragmentComentarios.this));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            showAlertDialog(rootView.getContext(), "Error", e.toString()+String.valueOf(turno),false);
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

    //Metodo para guardar los comentarios que provienen del servidor
    public void cargarComentarios(String datos){
        if(datos.length()>2) {
            JsonParser parser = new JsonParser();
            Object obje = parser.parse(datos);
            JsonArray array = (JsonArray) obje;
            //Validar existencia de datos
            if (!array.isJsonNull()) {
                List<TurnCome> listaComentarios=new ArrayList<TurnCome>();
                for (int x = 0; x < array.size(); x++) {
                    try{
                    TurnCome comentario= new TurnCome();
                    JsonObject objO = array.get(x).getAsJsonObject();
                    if (!(objO.get("comecons").isJsonNull())) {
                        comentario.setComecons(objO.get("comecons").getAsString());
                    }if (!(objO.get("comedesc").isJsonNull())) {
                        comentario.setComedesc(objO.get("comedesc").getAsString());
                    }if (!(objO.get("comedano").isJsonNull())) {
                        comentario.setComedano(objO.get("comedano").getAsString());
                    }if (!(objO.get("comeorde").isJsonNull())) {
                        comentario.setComeorde(objO.get("comeorde").getAsString());
                    }if (!(objO.get("comesoli").isJsonNull())) {
                        comentario.setComesoli(objO.get("comesoli").getAsString());
                    }if (!(objO.get("comequej").isJsonNull())) {
                        comentario.setComequej(objO.get("comequej").getAsString());
                    }if (!(objO.get("comerevi").isJsonNull())) {
                        comentario.setComerevi(objO.get("comerevi").getAsString());
                    }if (!(objO.get("comeuscr").isJsonNull())) {
                        comentario.setComeuscr(objO.get("comeuscr").getAsString());
                    }if (!(objO.get("comefecr").isJsonNull())) {
                        comentario.setComefecr(objO.get("comefecr").getAsString());
                    }if (!(objO.get("comeesta").isJsonNull())) {
                        comentario.setComeesta(objO.get("comeesta").getAsString());
                    }
                        List<TurnCome> comentarios= comentarioDao.queryBuilder().where(TurnComeDao.Properties.Comecons.in(comentario.getComecons())).list();
                        if(comentarios.size()<1){
                            comentario.setIdTurn(turno);
                            //comentarioDao.insert(comentario);
                            listaComentarios.add(comentario);
                        }
                    }catch (Exception e){
                        showAlertDialog(rootView.getContext(), "Comentario", e.toString(),false);
                    }
                }
                comentarioDao.insertInTx(listaComentarios);
                buscarMaterial();
            }
        }
        if (this.pd != null) {
            this.pd.dismiss();
        }
    }

    //Clase para consultar el servidor
    private class MiTareaPersonal extends AsyncTask<String, Float, String> {
        private String jsonObject;
        private final String HTTP_EVENT;
        private HttpClient httpclient;
        BufferedReader in = null;

        public MiTareaPersonal(String url,String jsonObject){
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
            cargarComentarios(tiraJson);
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