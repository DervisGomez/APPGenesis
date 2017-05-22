package org.app.atenciondeordenes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.app.appgenesis.R;
import org.app.appgenesis.dao.*;
import org.app.appgenesis.dao.Dibujo;
import org.app.appgenesis.dao.Firma;

import java.util.ArrayList;
import java.util.List;

public class VerFirma extends AppCompatActivity {
    private DibujoDao dibujoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_firma);
        getSupportActionBar().hide();
        final LinearLayout llFirmaMostrar=(LinearLayout) findViewById(R.id.llFimaMostrar);
        Button regresar=(Button)findViewById(R.id.btnRegresarMostrarFirma);
        TextView nombre=(TextView)findViewById(R.id.etNombreFirmaMostrar);
        TextView documento=(TextView)findViewById(R.id.etDocumentoFirmaMostrar);
        TextView correo=(TextView)findViewById(R.id.etCorreoFirmaMostrar);

        //Acción salir de vista de firma
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        DAOApp daoApp=new DAOApp();
        dibujoDao=daoApp.getDibujoDao();
        FirmaDao firmaDao=daoApp.getFirmaDao();

        //final PintarView b=new PintarView(this);
        List<org.app.appgenesis.dao.Dibujo> dibujo =new ArrayList<Dibujo>();
        try {
            Bundle bolsa=getIntent().getExtras();
            long orden=bolsa.getLong("orden");

            //Mostrar Datos
            Firma firma=firmaDao.load(orden);
            nombre.setText("NOMBRE: "+firma.getNombre());
            documento.setText("DOCUMENTO: "+firma.getDocumento());
            correo.setText("CORREO:"+firma.getCorreo());
            //dibujo=dibujoDao._queryGro_orden_Dibujo(orden);



            //Mostrar imagen
            Log.d("Firma:","["+ firma.getFoto()+"]");
            byte[] decodedString = Base64.decode(firma.getFoto(), Base64.DEFAULT);
            Bitmap mImageBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ImageView cimg = new ImageView(this);

//Now try setting dynamic image
            cimg.setImageBitmap(mImageBitmap);
            llFirmaMostrar.addView(cimg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*b.setDibujo(dibujo);
        llFirmaMostrar.addView(b);*/

    }
    class PintarView extends View {
        float x=50;
        float y=50;
        Context context;
        String accion= "nada";
        List<org.app.appgenesis.dao.Dibujo> dibujo=new ArrayList<org.app.appgenesis.dao.Dibujo>();
        int orden;

        Path path=new Path();

        public PintarView(Context context){
            super(context);
            this.context=context;

        }

        public void setDibujo(List<org.app.appgenesis.dao.Dibujo> dibujo){
            this.dibujo=dibujo;
            invalidate();
        }

        public PintarView(final Context context, List<org.app.appgenesis.dao.Dibujo> dibujo) {
            super(context);
            this.context=context;
            this.dibujo=dibujo;
            invalidate();

        }

        protected void onDraw(Canvas canvas){
            canvas.drawColor(Color.WHITE);
            Paint paint=new Paint();
            paint.setAntiAlias(true);

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1);
            paint.setColor(Color.BLACK);
            for (int i=0;i<dibujo.size();i++){
                if ("down".equals(dibujo.get(i).getAccion())){
                    path.moveTo(dibujo.get(i).getX(),dibujo.get(i).getY());
                }if("move".equals(dibujo.get(i).getAccion())){
                    path.lineTo(dibujo.get(i).getX(),dibujo.get(i).getY());
                }
                canvas.drawPath(path,paint);

            }
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
            //invalidate();
            return true;
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
