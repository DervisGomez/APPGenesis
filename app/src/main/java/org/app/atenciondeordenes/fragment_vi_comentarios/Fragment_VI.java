package org.app.atenciondeordenes.fragment_vi_comentarios;

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
import org.app.appgenesis.dao.Comentario;
import org.app.appgenesis.dao.ComentarioDao;
import org.app.appgenesis.dao.Gro_orden;
import org.app.appgenesis.dao.Gro_ordenDao;
import org.app.atenciondeordenes.ComentarioAdapter;
import org.app.atenciondeordenes.DAOApp;
import org.app.atenciondeordenes.VerificarConexion;
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

public class Fragment_VI extends Fragment {
    private List<Comentario> listaOrden=new ArrayList<Comentario>();
    private long orden;
    private EditText etNumero;
    private EditText etComentario;
    private Button btnRegresar;
    private Button btnGuardar;
    private Button btnNuevo;
    private View rootView;
    private RelativeLayout formulario;
    private ListView lvComentario;
    private ComentarioDao comentarioDao;
    private String estado;
    private Button btnDescargar;
    private Globals globals = Globals.getInstance();
    private ProgressDialog pd = null;

    public Fragment_VI() {
        // Required empty public constructor
    }

    public String getEstado() {
        return estado;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment__vi, container, false);
        etNumero=(EditText)rootView.findViewById(R.id.etNumeroComentario);
        etComentario=(EditText)rootView.findViewById(R.id.etComentario);
        btnRegresar=(Button)rootView.findViewById(R.id.btnRegresarComentario);
        btnGuardar=(Button)rootView.findViewById(R.id.btnGuardarComentario);
        btnNuevo=(Button)rootView.findViewById(R.id.btnNuevoComentario);
        formulario=(RelativeLayout) rootView.findViewById(R.id.formularioComentario);
        lvComentario=(ListView) rootView.findViewById(R.id.lvComentarioMostrar);
        btnDescargar=(Button)rootView.findViewById(R.id.btnDescargarComentario);
            formulario.setVisibility(View.GONE);
            DAOApp daoApp=new DAOApp();
            comentarioDao=daoApp.getComentarioDao();
        orden= getArguments().getLong("id");
        Gro_orden orden1= new Gro_orden();
        Gro_ordenDao ordenDao= daoApp.getGro_ordenDao();
        orden1=ordenDao.load(orden);

        //Habilita formulario de nuevo comentario
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!estado.equals("Cerrada")){
                    if(estado.equals("Iniciada")){
                        formulario.setVisibility(View.VISIBLE);
                    }else{
                        showAlertDialog(rootView.getContext(), "Error", "Para realizar esta acción la orden debe estar en estado INICIADA",false);
                    }
                }else{
                    showAlertDialog(rootView.getContext(), "Error", "Esta orden no se puede editar porque esta CERRADA",false);
                }

            }
        });
        final Gro_orden finalOrden = orden1;

        //Acción guardar comentario
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numero=etNumero.getText().toString();
                String comentario=etComentario.getText().toString();

                //valida que los campos no esten vacios
                if(numero.length()>0&&comentario.length()>0){
                    try {
                        Comentario comentario1= new Comentario();
                        comentario1.setComedano(String.valueOf(finalOrden.getORDECONS()));
                        comentario1.setComedesc(comentario);
                        comentario1.setIdOrden(orden);
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

        //Acción deshabilitar el formulario nuevo comentario
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulario.setVisibility(View.GONE);
                etNumero.setText("");
                etComentario.setText("");
            }
        });

        //Accion descargar comentarios desde el servidor
        btnDescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Verficicar si tiene conexion wifi o datos
                boolean conexion=new VerificarConexion(rootView.getContext()).estaConectado();
                if(conexion){
                    //Verificar si la orden esta iniciada
                    if (estado.equals("Iniciada")){
                        pd = ProgressDialog.show(rootView.getContext(), "Procesando", "Espere unos segundos...", true, false);
                        JSONObject jsonObject = new JSONObject();
                        try {
                            DAOApp dao=new DAOApp();
                            Gro_ordenDao gro_ordenDao=dao.getGro_ordenDao();
                            Gro_orden gro_orden=gro_ordenDao.load(orden);
                            jsonObject.put("ordenudo",gro_orden.getORDENUDO());
                            jsonObject.put("ordetido",gro_orden.getTIPODOCU());
                            new MiTareaPersonal(globals.getServer()+"/GenadminOp/gmacomentario/findByType",jsonObject.toString()).execute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        showAlertDialog(rootView.getContext(), "Orden", "Para descargar la orden debe estar iniciada",false);
                    }
                }else{
                    showAlertDialog(rootView.getContext(), "Error", "No tienes Conexión en este momento",false);
                }

            }
        });

        estado =orden1.getESTADO();
        etNumero.setText("Orden #"+String.valueOf(orden1.getORDECONS()));
        buscarMaterial();

        Log.d("Framento:", "FragmentVI_load");

        return rootView;
    }

    //Cargar lista de Materiales
    public void buscarMaterial(){
        try {
            listaOrden=comentarioDao._queryGro_orden_Comentario(orden);
            this.lvComentario.setAdapter(new ComentarioAdapter(rootView.getContext(), listaOrden,Fragment_VI.this));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            showAlertDialog(rootView.getContext(), "Error", e.toString(),false);
        }
    }

    //Metodo que muestra las alertas
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

    //Metodo que guarda comentarios traidos del servidor
    public void cargarComentarios(String datos){
        if(datos.length()>2) {
            JsonParser parser = new JsonParser();
            Object obje = parser.parse(datos);
            JsonArray array = (JsonArray) obje;
            //Valida que existan datos
            if (!array.isJsonNull()) {
                List<Comentario> listaComentario=new ArrayList<Comentario>();
                for (int x = 0; x < array.size(); x++) {
                    try{
                    Comentario comentario= new Comentario();
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
                        List<Comentario> comentarios= comentarioDao.queryBuilder().where(ComentarioDao.Properties.Comecons.in(comentario.getComecons())).list();
                        if(comentarios.size()<1){
                            comentario.setIdOrden(orden);
                            //comentarioDao.insert(comentario);
                            listaComentario.add(comentario);
                        }
                    }catch (Exception e){
                        showAlertDialog(rootView.getContext(), "Comentario", e.toString(),false);
                    }
                }
                comentarioDao.insertInTx(listaComentario);
                buscarMaterial();
            }
        }
        if (this.pd != null) {
            this.pd.dismiss();
        }
    }

    //Clase que consulta el servidor por POST
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

        //Accion que se ejecuta luego de consutar el servidor
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