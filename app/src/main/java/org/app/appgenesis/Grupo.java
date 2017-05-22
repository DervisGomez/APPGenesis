package org.app.appgenesis;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import org.app.administrador_sql.MainActivityAdministradorSQL;
import org.app.atenciondeordenes.*;
import org.app.atenciondeturnos.main.ListadoTurno;

import java.util.List;

public class Grupo extends AppCompatActivity implements OnClickListener {
    private TextView tvTitulo;
    private ImageButton btnL1,btnL2,btnL3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);
        tvTitulo = (TextView)findViewById(R.id.tvTitulo);
        btnL1=(ImageButton)findViewById(R.id.btnL1);
        btnL1.setOnClickListener(this);
        btnL2=(ImageButton)findViewById(R.id.btnL2);
        btnL2.setOnClickListener(this);
        btnL3=(ImageButton)findViewById(R.id.btnAtencionTurnos);
        btnL3.setOnClickListener(this);
        Bundle bolsa=getIntent().getExtras();
        String titulo = bolsa.getString("titulo");
        tvTitulo.setText(titulo);
        getSupportActionBar().hide();/*
        Display d=getWindowManager().getDefaultDisplay();
        int x=d.getWidth();
        int p=(x-106)/3;
        RelativeLayout.LayoutParams a=new RelativeLayout.LayoutParams(p,p);
        btnL1.setLayoutParams(a);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Globals g=Globals.getInstance();
        if(g.getAccess_token()==null){
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnL1:
                Intent ListSong = new Intent(this, ListadoDeOrdenes.class);
                startActivityForResult(ListSong,1);
                //startActivity(ListSong);
                break;
            case R.id.btnL2:
                Intent ListSong2 = new Intent(this, MainActivityAdministradorSQL.class);
                startActivityForResult(ListSong2,1);
                break;
            case R.id.btnAtencionTurnos:
                Intent ListSong3= new Intent(this, ListadoTurno.class);
                startActivityForResult(ListSong3,1);
                break;
        }
    }
    public void abrirAPP(String pack, String packpack, String category){
        Intent intent5 = new Intent();
        intent5.setComponent(new ComponentName(pack,packpack));
        intent5.addCategory(category);
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent5, 0);
        boolean isIntentSafe = activities.size() > 0;
        if(isIntentSafe){
            startActivity(intent5);
        }else{
            showAlertDialog(this, "APP", "No tiene esta aplición instalada",false);
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
}
