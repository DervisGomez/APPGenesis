package org.app.appgenesis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.app.atenciondeordenes.VerificarConexion;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Policy;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private EditText etUsuario;
    private EditText etClave;
    private Button btnEntrar;
    int control=0;
    private ProgressDialog pd = null;
    Globals globals = Globals.getInstance();

    /*
    eNoFwceia0AAANC9X8mC6BZvoWcIV4u2U0YfPYSvf+eUqeflmsVH0ol0PpGpbGU2xS2Cy3dvro/jtyc6QEs7YS8qAWhU+lqDmP5TmfsU9yxo2CrECwAcioMYzsDvu15nxJFjzJrn9OkGrzOzpqdgUk2MqLauxMHnnEKey0ZBIVA/d7QxKBPzXrZST99TVl7Wr6wSzLxVdzq3of7erT24aMdVokFvx+w4v7RnfacP4Pt8+HG4X2tSPwADomIZYa7xZMX5W6oKYyRsl33FNjY4crOzl3zg+RtkL8lPPguB8GP/ZE3UfkfFwwvHZeZ0q6UB4qVJ/vYyQ6ZmUqBtY0E8pwr9laronDEGSxrCpo7CP8MXUGGuJSAb+7UjSkiC9SEV22kdYqe64+4dxm/mYzv5pCrJX82Wx9HcNv5+gWnAXcvEXkw3vgGoZV2en2OoOOnActeD25igAuCnSch+nloFMg1wvfPHahcUPzE45Eh3siXDu5eHIFDYrXZkjOYDIueGJzCsuzncdcqXzzqgrhqPqSHN6ysOukt9+0FIbPchgTKbwil67bf1JWDxjggQKA7aMlZGFxaGrOUfm9E/smgIf2JatIBxeTDDltla7RE7hyY5LMFW1idxynXXlhwdy7oR25T7VmZ29Qd28LF4kMeoPMmGjdygpXOHkv/FX0+zhdtFzpq0uNy6QtE3idc+8ZcKaokn/WNScG15ShUrTMPTz82uf+5uw7xIzJ72QHLvU7uGcAmie5oSfBGyY0FJnaFArXpk+UYsbu5sT0QlgvPpGGg315aRd7afFOpkvy0ZH8gxMZmp41HXQ5gnMYuvkPpajzU4aEiFkRfPo3Umtr1y3lqJWdgWm2gvCR7Dqz/39VuEMkIKrnddmJM1iQW5Gjk/4fEVmf6N3lYpW7/7WgaTpD1xIeFFPxWWr30BGkpK5CztXq1vuAqTl3/4e/PoJdUKYXqEqpthoVrvbrVfEbLU5G8nyZqh8ef2+XNosrBRrrsffdzHszNMhjUgf/+ORWCWQeS1fElVON8XpZz//v0HzHBNdg==
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etClave = (EditText) findViewById(R.id.etClave);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(this);

        if(globals.getServer()==null || globals.getServer().isEmpty()){
            Log.d("Info","Se ha creado valor para Server");
            globals.setServer("https://appexp.akc.co/akc/genesis");
        }
        if(globals.getServer_token()==null || globals.getServer_token().isEmpty())
            Log.d("Info","Se ha creado valor para Server Token");
        globals.setServer_token("https://appexp.akc.co/akc/uaa/oauth/token");


        getSupportActionBar().hide();
        campoText();
        Display d=getWindowManager().getDefaultDisplay();
        int x=d.getWidth();
        int p=((x-192)/2);
        btnEntrar.setPadding(p,0,p,0);
    }

    public void campoText(){
        etUsuario.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        etUsuario.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event)
            {
                boolean action = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT)
                {
                    // hide keyboard
                    //InputMethodManager inputMethodManager = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    //inputMethodManager.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    etClave.requestFocus();
                    action = true;
                }
                return action;
            }
        });

        etClave.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event)
            {
                boolean action = false;
                if (actionId == EditorInfo.IME_ACTION_GO)
                {
                    onClick(btnEntrar);
                    action = true;
                }
                return action;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEntrar:
                String usuario = etUsuario.getText().toString();
                String clave = etClave.getText().toString();
                if (usuario.length() > 0 && clave.length() > 0) {
                    if(usuario.equals("Administrador") && clave.equals("123")){
                        Intent ListSong = new Intent(getApplicationContext(), Administrativa.class);
                        startActivity(ListSong);
                    }else{
                        //Verficicar si tiene conexion wifi o datos
                        boolean conexion=new VerificarConexion(this).estaConectado();
                        if(conexion){
                            pd = ProgressDialog.show(this, "Proceso", "Iniciando sesión. Espere unos segundos...", true, false);
                            new MiTareaToken(globals.getServer_token(), "grant_type=password&username="+usuario+"@akc.co"+"&password="+clave).execute();

                            //Intent ListSong = new Intent(getApplicationContext(), Categoria.class);
                            //startActivity(ListSong);
                        }else{
                            showAlertDialog(this, "Error", "No tienes Conexión en este momento",false);
                        }
                    }
                }else{
                    showAlertDialog(this, "Login", "Debe especificar el usuario y contraseña",false);
                }
                break;
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

    public void mensaje(String dato){
        showAlertDialog(this, "Orden", dato,true);
    }


    /**************** CONSEGUIR EL TOKEN ****************************/

    private class MiTareaToken extends AsyncTask<String, Float, String> {

        private String jsonObject;
        private final String HTTP_EVENT;
        private HttpClient httpclient;
        BufferedReader in = null;

        public MiTareaToken(String url, String jsonObject) {
            Log.d("url",url);
            this.HTTP_EVENT = url;
            this.jsonObject = jsonObject;
        }

        protected void onPreExecute() {

        }

        protected String doInBackground(String... urls) {
            String resul = "";
            try {

                //Creamos un objeto Cliente HTTP para manejar la peticion al servidor
                HttpClient httpClient = new DefaultHttpClient();
                //Creamos objeto para armar peticion de tipo HTTP POST
                HttpPost post = new HttpPost(HTTP_EVENT);

                //Configuramos los parametos que vaos a enviar con la peticion HTTP POST
                StringEntity stringEntity = new StringEntity(jsonObject);
                post.setHeader("Content-type", "application/x-www-form-urlencoded");
                post.setHeader("Authorization", "Basic Z2VuZXNpczpnZW5lc2lzQWRtaW4=");

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
                resul = sb.toString();

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return resul;
        }

        protected void onProgressUpdate(Float... valores) {

        }

        protected void onPostExecute(String json) {
            accederToken(json);

        }

        public void accederToken(String json){
            //mensaje(json);
            JsonParser parser = new JsonParser();
            JsonObject res = (JsonObject) parser.parse(json);

            if(res.get("error")!=null && !res.get("error").isJsonNull()){
                if (pd != null) {
                    pd.dismiss();
                }
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Error");
                //alertDialog.setMessage(res.get("error_description").toString());
                alertDialog.setMessage("Usuario y clave incorrectos");
                alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
                etClave.requestFocus();

            }else{


                globals.setAccess_token(res.get("access_token").toString().replace("\"",""));
                globals.setToken_type(res.get("token_type").toString().replace("\"",""));
                globals.setRefresh_token(res.get("refresh_token").toString().replace("\"",""));
                globals.setExpires_in(Long.parseLong(res.get("expires_in").toString().replace("\"","")));
                globals.setScope(res.get("scope").toString().replace("\"",""));
                globals.setJti(res.get("jti").toString().replace("\"",""));
                //g.setServer("186.28.231.50");https://


                Log.d("Globals",globals.toString());

                // Toast.makeText(MainActivity.this,"Bienvenido " + etUsuario.getText(),Toast.LENGTH_LONG);
                String usuario = etUsuario.getText().toString();
                String clave = etClave.getText().toString();
                etUsuario.setText("");
                etClave.setText("");
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user",usuario);
                    jsonObject.put("pass",clave);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new MiTareaPersonal(globals.getServer()+"/genUtilitario/util/profiles",jsonObject.toString()).execute();
                control++;
            }
        }
        public  void datosUsuario(String json){
            mensaje(json);
        }

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

    protected void onPostExecute(String datos) {
        if (pd != null) {
            pd.dismiss();
        }

        try {
            JsonParser parser = new JsonParser();
            Object obje = parser.parse(datos);
            JsonObject array=(JsonObject)obje;
            if(!array.isJsonNull()) {
                JsonObject array2=array.get("usuario").getAsJsonObject();
                globals.setUsuario_cons(array2.get("usuacons").getAsString());
                globals.setUsuario_dominio(array2.get("usuausdo").getAsString());
                Intent ListSong = new Intent(getApplicationContext(), Categoria.class);
                startActivity(ListSong);

            }

        }catch (Exception e){

        }
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
