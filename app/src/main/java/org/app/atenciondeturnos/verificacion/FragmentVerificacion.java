package org.app.atenciondeturnos.verificacion;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import org.app.appgenesis.dao.Dibujo;
import org.app.appgenesis.dao.DibujoDao;
import org.app.appgenesis.dao.Firma;
import org.app.atenciondeordenes.DAOApp;
import org.app.atenciondeordenes.FirmaAdapter;
import org.app.atenciondeordenes.MiServicio;
import org.app.atenciondeordenes.fragment_v_firmas.Fragment_V;
import org.app.atenciondeturnos.dao.TurnFirm;
import org.app.atenciondeturnos.dao.TurnFirmDao;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class FragmentVerificacion extends Fragment {
    LinearLayout firma;
    EditText etUsuario;
    EditText etNombre;
    EditText etTurno;
    Button btnRegresar;
    Button btnGuardar;
    Button btnAgregar;
    Button btnLimpiar;
    LinearLayout formulario;
    ListView lista;
    long orden;
    List<TurnFirm> firmas;
    View rootView;
    private Globals globals = Globals.getInstance();

    public FragmentVerificacion() {
        // Required empty public constructor
    }

    public Long getTurno(){
        return orden;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_turno_verificacion, container, false);
        firma=(LinearLayout)rootView.findViewById(R.id.llFimaTurno);
        etUsuario=(EditText)rootView.findViewById(R.id.etCodigoUsuarioTurno);
        etNombre=(EditText)rootView.findViewById(R.id.etNombreUsuarioTurno);
        etTurno=(EditText)rootView.findViewById(R.id.etTurnoTurno);
        btnRegresar=(Button)rootView.findViewById(R.id.btnRegresarFirmaTurno);
        btnGuardar=(Button)rootView.findViewById(R.id.btnGuardarFirmaTurno);
        btnAgregar=(Button)rootView.findViewById(R.id.btnNuevaVerifiacionTurno);
        btnLimpiar=(Button)rootView.findViewById(R.id.btnLimpiarFirmaTurno);
        formulario=(LinearLayout)rootView.findViewById(R.id.formularioVerificacionTurno);
        lista=(ListView)rootView.findViewById(R.id.lvFirmaMostrarTurno);
        orden= getArguments().getLong("id");
        etTurno.setText("Turno #"+String.valueOf(orden));
        formulario.setVisibility(View.GONE);

        //Accion mostrar formulario
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulario.setVisibility(View.VISIBLE);
                cargarFirma();
            }
        });

        //Accion lismpiar area de firma
        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarFirma();
            }
        });
        buscarTurnos(orden);
        return rootView;
    }

    //metodo cargar lista de verificaciones asociadas al turno
    public void buscarTurnos(long orden){
        DAOApp daoApp=new DAOApp();
        TurnFirmDao turnFirmDao=daoApp.getTurnFirmDao();

        try {
            firmas=turnFirmDao._queryGDB_TURNOS_Firma(orden);
            //showAlertDialog(rootView.getContext(),"hola",String.valueOf(firmas.size()),false);

            //llamar adapter para mostrar listados de turno en la vista
            this.lista.setAdapter(new TurnoVerificacionAdapter(rootView.getContext(), firmas,FragmentVerificacion.this));
        } catch (Exception e) {
            // TODO Auto-generated catch block

        }
    }

    //metodo cargar area de la firma
    public void cargarFirma(){
        PintarView b=new PintarView(rootView.getContext(),etUsuario,etNombre,btnGuardar,btnRegresar,orden,formulario,lista,firma);
        firma.removeAllViews();
        btnLimpiar.setVisibility(View.VISIBLE);
        btnGuardar.setVisibility(View.VISIBLE);
        etUsuario.setFocusableInTouchMode(true);
        etNombre.setFocusableInTouchMode(true);
        firma.addView(b);
    }

    //metodo mostrar detalle de una verificacion
    public void llamarVerFirma(long x){
        try {
            DAOApp daoApp=new DAOApp();
            TurnFirmDao turnFirmDao=daoApp.getTurnFirmDao();
            TurnFirm turnFirm=turnFirmDao.load(x);
            firma.removeAllViews();
            etUsuario.setText(turnFirm.getCodigo());
            etUsuario.setFocusable(false);
            btnLimpiar.setVisibility(View.GONE);
            btnGuardar.setVisibility(View.GONE);
            etNombre.setText(turnFirm.getNombre());
            etNombre.setFocusable(false);
            formulario.setVisibility(View.VISIBLE);
            byte[] decodedString = Base64.decode(turnFirm.getFoto(), Base64.DEFAULT);
            Bitmap mImageBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ImageView cimg = new ImageView(rootView.getContext());

//Now try setting dynamic image
            cimg.setImageBitmap(mImageBitmap);
            firma.addView(cimg);
        }catch (Exception e){
            showAlertDialog(rootView.getContext(),"Error",e.toString(),false);
        }

    }

    //Clase para cargar area de la firma
    class PintarView extends View {
        float x=50;

        float y=50;
        Context context;
        String accion= "nada";
        LinearLayout firma;
        EditText etCodigo;
        EditText etNombre;
        Button btnRegresar;
        Button btnGuardar;
        EditText etCorreo;
        LinearLayout formulario;
        List<org.app.appgenesis.dao.Dibujo> dibujo=new ArrayList<org.app.appgenesis.dao.Dibujo>();
        long orden;
        ListView lvFirmas;
        Fragment_V fragment;
        TurnFirmDao firmaDao;
        DibujoDao dibujoDao;
        Path path=new Path();
        Canvas canvas2=new Canvas();
        boolean can=true;
        Bitmap bitmap;
        ListView lista;

        public PintarView(Context context,Button button){
            super(context);
            this.context=context;

        }

        public PintarView(final Context context, final EditText etCodigo, final EditText etNombre, Button btnGuardar, Button btnRegresar, final long orden, LinearLayout llFirmas, final ListView lista, final LinearLayout firma) {
            super(context);
            this.context=context;
            this.etCodigo=etCodigo;
            this.lista=lista;
            this.etNombre=etNombre;
            this.btnGuardar=btnGuardar;
            this.btnRegresar=btnRegresar;
            this.orden=orden;
            this.firma=firma;
            this.etCorreo=etCorreo;
            this.formulario=llFirmas;
            this.fragment=fragment;
            DAOApp daoApp=new DAOApp();
            firmaDao = daoApp.getTurnFirmDao();
            dibujoDao = daoApp.getDibujoDao();

            //Accion salir del formulario
            this.btnRegresar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    etNombre.setText("");
                    etCodigo.setText("");
                    formulario.setVisibility(View.GONE);

                }
            });

            //Accion guardar datos de nueva verificacion
            this.btnGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String nombre=etNombre.getText().toString();
                    String codigo=etCodigo.getText().toString();
                    String foto = "a";
                    try {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        firma.setDrawingCacheEnabled(true);
                        firma.buildDrawingCache();
                        Bitmap bm = firma.getDrawingCache();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        foto = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    } catch (Exception e) {
                        showAlertDialog(context, "Error", e.toString(),false);
                    }
                    //String correo=etCorreo.getText().toString();
                    if (dibujo.size()>0&&nombre.length()>0&&codigo.length()>0){
                        firma.removeAllViews();
                        TurnFirm firma = new TurnFirm();
                        firma.setCodigo(codigo);
                        firma.setNombre(nombre);
                        firma.setIdTurno(orden);
                        firma.setEstado("Activo");
                        firma.setFoto(foto);
                        Calendar fech = new GregorianCalendar();
                        Date date=new Date(fech.getTimeInMillis());
                        firma.setUsuario("");
                        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        firma.setFecha(df2.format(date));
                        firmaDao.insert(firma);

                        /*for (int x=0;x<dibujo.size();x++){
                            Dibujo dibuj = dibujo.get(x);
                            dibuj.setIdFirma(f);
                            dibujoDao.insert(dibuj);
                        }*/
                        buscarTurnos(orden);
                        etNombre.setText("");
                        etCodigo.setText("");
                        formulario.setVisibility(View.GONE);
                        mensajeGuardar();
                    }else {
                        mensajeError();
                    }
                }
            });
        }

        public void mensajeError(){
            showAlertDialog(context, "Error","Debe especificar firma, codigo y nombre",false);
        }
        public void mensajeGuardar(){
            showAlertDialog2(context, "Verificación","Registro guardado exitosamente",false);
        }

        protected void onDraw(Canvas canvas){
            canvas.drawColor(Color.WHITE);
            Paint paint=new Paint();
            paint.setAntiAlias(true);

            if(can){
                bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
                canvas2.setBitmap(bitmap);
                can=false;


            }

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1);
            paint.setColor(Color.BLACK);
            if (accion=="down"){
                path.moveTo(x,y);
                dibujar(x,y,"down");
            }if(accion=="move"){
                path.lineTo(x,y);
                dibujar(x,y,"move");
            }
            canvas.drawPath(path,paint);
            canvas2.drawPath(path,paint);
        }
        public void dibujar(float x, float y, String h){
            org.app.appgenesis.dao.Dibujo d=new Dibujo();
            d.setX(x);
            d.setY(y);
            d.setAccion(h);
            dibujo.add(d);
        }
        public boolean onTouchEvent(MotionEvent event){
            int axion = event.getAction();
            x=event.getX();
            y=event.getY();

            if (axion==MotionEvent.ACTION_DOWN){
                accion="down";
            }if (axion==MotionEvent.ACTION_MOVE){
                accion="move";
            }
            //showAlertDialog(context, accion, "X:"+String.valueOf(x)+" Y:"+String.valueOf(y),false);
            invalidate();
            return true;
        }
        public void showAlertDialog2(Context context, String title, String message, Boolean status) {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle(title);
            alertDialog.setMessage(message);
//		alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
            alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //finish();
                }
            });

            alertDialog.show();
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

    protected void onPostExecute(String tiraJson) {


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
