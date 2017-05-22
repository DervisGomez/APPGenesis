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
import android.widget.Button;
import android.widget.TextView;


import java.util.List;

public class Grupo2 extends AppCompatActivity implements OnClickListener {
    private TextView tvTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.app.appgenesis.R.layout.activity_grupo2);
        tvTitulo = (TextView)findViewById(R.id.tvTitulo2);
        Bundle bolsa=getIntent().getExtras();
        String titulo = bolsa.getString("titulo");
        tvTitulo.setText(titulo);
        getSupportActionBar().hide();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

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
