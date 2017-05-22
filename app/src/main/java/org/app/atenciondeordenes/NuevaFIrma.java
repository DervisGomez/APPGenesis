package org.app.atenciondeordenes;

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
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.app.appgenesis.Globals;
import org.app.appgenesis.R;
import org.app.appgenesis.dao.*;
import org.app.appgenesis.dao.Dibujo;
import org.app.appgenesis.dao.Firma;
import org.app.atenciondeordenes.fragment_v_firmas.Fragment_V;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class NuevaFIrma extends AppCompatActivity {
    LinearLayout firma;
    EditText etDocumento;
    EditText etNombre;
    EditText etCorreo;
    Button btnRegresar;
    Button btnGuardar;
    Button btnLimpiar;
    long orden, ordecons;
    private Globals globals = Globals.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.nueva_firma);
        firma=(LinearLayout)findViewById(R.id.llFima);
        etDocumento=(EditText)findViewById(R.id.etDocumentoFirma);
        etNombre=(EditText)findViewById(R.id.etNombreFirma);
        etCorreo=(EditText)findViewById(R.id.etCorreoFirma);
        btnRegresar=(Button)findViewById(R.id.btnRegresarFirma);
        btnGuardar=(Button)findViewById(R.id.btnGuardarFirma);
        btnLimpiar=(Button)findViewById(R.id.btnLimpiarFirma);
        Bundle bolsa=getIntent().getExtras();

        orden=bolsa.getLong("orden");

        //Acción limpiar area de la firma
        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarTodo();
            }
        });

        cargarTodo();
    }

    //Cargar vista de la firma
    public void cargarTodo(){
        PintarView b=new PintarView(this,etDocumento,etNombre,btnGuardar,btnRegresar,orden,etCorreo,firma);
        firma.removeAllViews();
        firma.addView(b);
    }
    class PintarView extends View {
        float x=50;
        float y=50;
        Context context;
        String accion= "nada";
        LinearLayout firma;
        EditText etDocumento;
        EditText etNombre;
        Button btnRegresar;
        Button btnGuardar;
        EditText etCorreo;
        RelativeLayout formulario;
        List<org.app.appgenesis.dao.Dibujo> dibujo=new ArrayList<org.app.appgenesis.dao.Dibujo>();
        long orden;
        ListView lvFirmas;
        Fragment_V fragment;
        FirmaDao firmaDao;
        DibujoDao dibujoDao;
        Path path=new Path();
        Canvas canvas2=new Canvas();
        boolean can=true;
        Bitmap bitmap;
        //private ProgressDialog pd = null;

        public PintarView(Context context,Button button){
            super(context);
            this.context=context;

        }

        //Clase para generar el espacio de la firma
        public PintarView(final Context context, final EditText etDocumento, final EditText etNombre, Button btnGuardar, Button btnRegresar, final long orden, final EditText etCorreo, final LinearLayout firma) {
            super(context);
            this.context=context;
            this.etDocumento=etDocumento;
            this.etNombre=etNombre;
            this.btnGuardar=btnGuardar;
            this.btnRegresar=btnRegresar;
            this.orden=orden;
            this.etCorreo=etCorreo;
            this.firma=firma;
            this.lvFirmas=lvFirmas;
            this.fragment=fragment;
            DAOApp daoApp=new DAOApp();
            firmaDao = daoApp.getFirmaDao();
            dibujoDao = daoApp.getDibujoDao();

            //Accion salir del formulario
            this.btnRegresar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            //Acción Guardar firma
            this.btnGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // pd = ProgressDialog.show(context, "Procesando", "Espere unos segundos...", true, false);
                    String nombre=etNombre.getText().toString();
                    String documento=etDocumento.getText().toString();
                    String foto = "a";

                    try {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        /*bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        foto = Base64.encodeToString(byteArray, Base64.DEFAULT);*/
                        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        firma.setDrawingCacheEnabled(true);
                        firma.buildDrawingCache();
                        Bitmap bm = firma.getDrawingCache();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        foto = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        //firma.setVisibility(View.GONE);
                    } catch (Exception e) {
                        showAlertDialog(context, "Error", e.toString(),false);
                    }

                    String correo=etCorreo.getText().toString();
                    if (nombre.length()>0 && documento.length()>0){
                        Firma firma = new Firma();
                        firma.setDocumento(documento);
                        firma.setNombre(nombre);
                        firma.setIdOrden(orden);
                        firma.setEstado("Activo");
                        firma.setCorreo(correo);
                        firma.setFoto(foto.replace("\n",""));
                        //etCorreo.setText(foto);
                        Calendar fech = new GregorianCalendar();
                        Date date=new Date(fech.getTimeInMillis());
                        firma.setUsuario(globals.getUsuario_dominio());
                        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        firma.setFecha(df2.format(date));
                        long f=firmaDao.insert(firma);

                        /*for (int x=0;x<dibujo.size();x++){
                            Dibujo dibuj = dibujo.get(x);
                            dibuj.setIdFirma(f);
                            dibujoDao.insert(dibuj);
                        }*/
                        //mensajeGuardar();

                        mensajeGuardar();
                        finish();



                    }else {
                        mensajeError();
                    }
                }
            });
        }

        public void mensajeError(){
            showAlertDialog(context, "Error","Debe especificar firma, documento y nombre",false);
        }
        public void mensajeGuardar(){
            showAlertDialog2(context, "Firma","Registro guardado exitosamente",false);
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
            }if(accion=="move"){
                path.lineTo(x,y);
            }
            canvas.drawPath(path,paint);
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
                    finish();
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
}
