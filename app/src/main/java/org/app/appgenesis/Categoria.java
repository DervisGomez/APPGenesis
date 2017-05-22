package org.app.appgenesis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class Categoria extends AppCompatActivity implements OnClickListener{
    private ImageButton btnOperativo;
    private ImageButton btnComercial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        btnOperativo=(ImageButton)findViewById(R.id.btnOperativo);
        btnComercial=(ImageButton)findViewById(R.id.btnComercial);
        btnOperativo.setOnClickListener(this);
        btnComercial.setOnClickListener(this);
        getSupportActionBar().hide();/*
        Display d=getWindowManager().getDefaultDisplay();
        int x=d.getWidth();
        int p=(x-74)/2;
        RelativeLayout.LayoutParams a=new RelativeLayout.LayoutParams(p,p);
        btnComercial.setLayoutParams(a);
        btnOperativo.setLayoutParams(a);*/
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
            case R.id.btnOperativo:
                Intent ListSong = new Intent(getApplicationContext(), Grupo.class);
                ListSong.putExtra("titulo","Procesos Operativo");
                startActivityForResult(ListSong,1);
                break;
            case R.id.btnComercial:
                Intent ListSong2 = new Intent(getApplicationContext(), Grupo2.class);
                ListSong2.putExtra("titulo","Procesos Comercial");
                startActivityForResult(ListSong2,1);
                break;
            case 2:
                Intent ListSong3 = new Intent(getApplicationContext(), Grupo2.class);
                ListSong3.putExtra("titulo","Procesos Gerencial");
                startActivityForResult(ListSong3,1);
                break;
        }
    }
}
