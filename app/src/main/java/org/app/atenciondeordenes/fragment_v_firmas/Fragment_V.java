package org.app.atenciondeordenes.fragment_v_firmas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.app.appgenesis.dao.Firma;
import org.app.appgenesis.dao.FirmaDao;

import org.app.appgenesis.R;
import org.app.appgenesis.dao.Gro_orden;
import org.app.appgenesis.dao.Gro_ordenDao;
import org.app.atenciondeordenes.fragment_viii_fotografias.ControlFotografia;
import org.app.atenciondeordenes.DAOApp;
import org.app.atenciondeordenes.FirmaAdapter;
import org.app.atenciondeordenes.NuevaFIrma;
import org.app.atenciondeordenes.VerFirma;

import java.util.List;

public class Fragment_V extends Fragment implements View.OnClickListener{

    Button btnNueva;
    View rootView;
    ListView lvFirmas;
    FirmaDao firmaDao;
    long ordenn;
    private String estado;
    private ControlFotografia controlFotografia=ControlFotografia.getInstance();

    public Fragment_V() {
        // Required empty public constructor
    }

    public String getEstado() {
        return estado;
    }

    public long getOrden(){
        return ordenn;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_v, container, false);

        btnNueva=(Button)rootView.findViewById(R.id.btnNuevaFirma);
        lvFirmas=(ListView)rootView.findViewById(R.id.lvFirmaMostrar);
        final Long orden= getArguments().getLong("id");

        ordenn=orden;
        DAOApp daoApp=new DAOApp();
        firmaDao = daoApp.getFirmaDao();
        buscarOrdenes(orden);
        Gro_ordenDao ordenDao = daoApp.getGro_ordenDao();
        Gro_orden miOrden = ordenDao.load(orden);
        estado = miOrden.getESTADO();

        //llama a la activity para agregar nueva firma
        btnNueva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!estado.equals("Cerrada")){
                    if (estado.equals("Iniciada")){
                        controlFotografia.setControlPrueba(0);
                        llamarActvity();
                    }else{
                        showAlertDialog(rootView.getContext(), "Error", "Para realizar esta acción la orden debe estar en estado INICIADA",false);
                    }
                }else{
                    showAlertDialog(rootView.getContext(), "Error", "Esta orden no se puede editar porque esta CERRADA",false);
                }

            }
        });

        Log.d("Framento:", "FragmentV_load");

        return rootView;
    }

    //metodo para llamar la activity de agregar firma
    public void llamarActvity(){
        Intent i=new Intent(rootView.getContext(), NuevaFIrma.class);

        i.putExtra("orden",ordenn);
        //startActivity(i);
        startActivityForResult(i,1);
    }

    //metodo para llamar la activity y ver firma
    public void llamarVerFirma(long xx){
        Intent i=new Intent(rootView.getContext(), VerFirma.class);

        i.putExtra("orden",xx);
        startActivity(i);
        //startActivityForResult(i,1);
    }

    //metodo que carga el listado de firmas
    public void buscarOrdenes(long orden){
        try {
            List<Firma> firmas=firmaDao._queryGro_orden_Firma(orden);
            //llama el adapter para cargar el listado en la interfaz
            this.lvFirmas.setAdapter(new FirmaAdapter(rootView.getContext(), firmas,Fragment_V.this));
        } catch (Exception e) {
            // TODO Auto-generated catch block

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

    @Override
    public void onClick(View view) {

    }


}