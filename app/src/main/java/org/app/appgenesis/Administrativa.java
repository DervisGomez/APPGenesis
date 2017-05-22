package org.app.appgenesis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Administrativa extends AppCompatActivity implements OnClickListener {

    private Button btnRegresar;
    private Button btnGuardar;
    private EditText txtServer;
    private EditText txtServerToken;
    private Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrativa);
        globals = Globals.getInstance();

        btnGuardar=(Button)findViewById(R.id.btnGuardar);
        btnRegresar=(Button)findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);
        txtServer = (EditText) findViewById(R.id.txtServer);
        txtServerToken = (EditText) findViewById(R.id.txtServerToken);

        txtServer.setText(globals.getServer());
        txtServerToken.setText(globals.getServer_token());

        getSupportActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnGuardar:
                guardarDatos();
                break;
            case R.id.btnRegresar:
                finish();
                break;
        }
    }

    private void guardarDatos() {
        Log.d("Info","Guardando datos... ");
        if(this.txtServer.getText().toString().isEmpty()){
            Log.d("Info","Servidor no puede estar vacío");
            Toast.makeText(Administrativa.this,"Servidor no puede estar vacío",Toast.LENGTH_LONG);
            return;
        }
        if(this.txtServerToken.getText().toString().isEmpty()){
            Log.d("Info","Servidor Token no puede estar vacío");
            Toast.makeText(Administrativa.this,"Servidor Token no puede estar vacío",Toast.LENGTH_LONG);
            return;
        }
        globals.setServer(this.txtServer.getText().toString());
        globals.setServer_token(this.txtServerToken.getText().toString());
        finish();
    }
}
