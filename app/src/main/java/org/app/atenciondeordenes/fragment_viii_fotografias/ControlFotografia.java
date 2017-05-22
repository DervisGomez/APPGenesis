package org.app.atenciondeordenes.fragment_viii_fotografias;

import android.net.Uri;

/**
 * Created by Administrador on 05/01/2017.
 */
public class ControlFotografia {

    private static ControlFotografia instance;

    private int controlPrueba;

    private byte[] imagen;

    private Uri uri;


    private ControlFotografia(){}

    public static void setInstance(ControlFotografia instance) {
        ControlFotografia.instance = instance;
    }

    public void setControlPrueba(int controlPrueba){
        this.controlPrueba=controlPrueba;
    }

    public int getControlPrueba(){return controlPrueba;}

    public void setImagen(byte[] imagen){ this.imagen=imagen; }

    public byte[] getImagen(){ return imagen; }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public static synchronized ControlFotografia getInstance(){
        if(instance==null){
            instance=new ControlFotografia();
        }
        return instance;
    }
}
