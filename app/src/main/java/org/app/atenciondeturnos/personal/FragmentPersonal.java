package org.app.atenciondeturnos.personal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;

import org.app.appgenesis.R;
import org.app.appgenesis.dao.Persona;
import org.app.appgenesis.dao.PersonaDao;
import org.app.atenciondeordenes.DAOApp;
import org.app.atenciondeturnos.dao.GDB_TURNOS;
import org.app.atenciondeturnos.dao.GDB_TURNOSDao;
import org.app.atenciondeturnos.dao.TurnPers;
import org.app.atenciondeturnos.dao.TurnPersDao;
import org.app.atenciondeturnos.dao.TurnPersTurn;
import org.app.atenciondeturnos.dao.TurnPersTurnDao;

import java.util.ArrayList;
import java.util.List;

public class FragmentPersonal extends Fragment implements OnClickListener {
    private List<TurnPers> listaOrden=new ArrayList<TurnPers>();
    private long orden;
    private AutoCompleteTextView etNombreApellido;
    private Button btnRegresar;
    private Button btnGuardar;
    private Button btnNuevaPersona;
    private View rootView;
    private RelativeLayout formulario;
    private ListView lvPersona;
    private TurnPersDao personaDao;
    private TurnPersTurnDao ordePersDao;
    private String estado;

    public FragmentPersonal() {
        // Required empty public constructor
    }

    public long getOrden() {
        return orden;
    }

    public String getEstado() {
        return estado;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_turno_personal, container, false);
        etNombreApellido=(AutoCompleteTextView)rootView.findViewById(R.id.etNombreApellidoTurno);
        btnRegresar=(Button)rootView.findViewById(R.id.btnRegresarPersonaTurno);
        btnGuardar=(Button)rootView.findViewById(R.id.btnGuardarPersonaTurno);
        btnNuevaPersona=(Button)rootView.findViewById(R.id.btnNuevaPersona2Turno);
        formulario=(RelativeLayout) rootView.findViewById(R.id.formularioPersonaTurno);
        formulario.setVisibility(View.GONE);
        lvPersona=(ListView)rootView.findViewById(R.id.lvPersonaMostrarTurno);
        DAOApp d=new DAOApp();
        personaDao=d.getTurnPersDao();
        ordePersDao=d.getTurnPersTurnDao();
        List<TurnPers> personas=new ArrayList<TurnPers>();
        personas=personaDao.loadAll();

        //Cargar AutoCompleteTextView de personal
        if (personas.size()>0){
            String[] auto=new String[personas.size()];
            for (int i=0;i<personas.size();i++){
                auto[i]=personas.get(i).getPersnomb();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(),
                    android.R.layout.simple_dropdown_item_1line, auto);
            etNombreApellido.setAdapter(adapter);
        }

        //Accion mostrar formulario para agregar personal
        btnNuevaPersona.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                formulario.setVisibility(View.VISIBLE);
                /*if(!estado.equals("Cerrada")){
                    if(estado.equals("Iniciada")){
                        formulario.setVisibility(View.VISIBLE);
                    }else{
                        showAlertDialog(rootView.getContext(), "Error", "Para realizar esta acción la orden debe estar en estado INICIADA",false);
                    }
                }else{
                    showAlertDialog(rootView.getContext(), "Error", "Esta orden no se puede editar porque esta CERRADA",false);
                }*/

            }
        });

        //Accion guardar personal al turno
        btnGuardar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre=etNombreApellido.getText().toString();

                if(nombre.length()>0){
                    try {
                        TurnPersTurn ordePers=new TurnPersTurn();
                        List<TurnPers> persona=personaDao.queryBuilder().where(TurnPersDao.Properties.Persnomb.in(nombre)).list();
                        if (persona.size()>0){
                            List<TurnPersTurn> ordePerses= ordePersDao.queryBuilder().where(TurnPersTurnDao.Properties.IdTurno.in(orden),TurnPersTurnDao.Properties.IdPersona.in(persona.get(0).getId())).list();
                            if(ordePerses.size()>0){
                                showAlertDialog(rootView.getContext(), "Error", "Este nombre ya esta agregado",false);
                            }else{
                                ordePers.setIdTurno(orden);
                                ordePers.setIdPersona(persona.get(0).getId());
                                ordePersDao.insert(ordePers);
                                buscarPersonas();
                                formulario.setVisibility(View.GONE);
                                etNombreApellido.setText("");
                            }
                        }else{
                            showAlertDialog(rootView.getContext(), "Error", "Nombre Invalido",false);
                        }

                    }catch (Exception e){
                        showAlertDialog(rootView.getContext(), "Error", e.toString(),false);
                    }

                }else{
                    showAlertDialog(rootView.getContext(), "Error", "Debe especificar el nombre",false);
                }
            }
        });

        //Accion salir del formulario agregar personal
        btnRegresar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                formulario.setVisibility(View.GONE);
                etNombreApellido.setText("");
            }
        });
        orden= getArguments().getLong("id");
        GDB_TURNOSDao ordenDao = d.getGdb_turnosDao();
        //estado =ordenDao.load(orden).getESTADO();
        buscarPersonas();

        // Register a callback to be invoked when an item in this AdapterView
        // has been clicked

        return rootView;
    }


    //Metodo cargar listado de personas asociadas al turno
    public void buscarPersonas(){
        try {
            List<TurnPers> personas=new ArrayList<TurnPers>();
            List<TurnPersTurn> ordePerses=ordePersDao._queryGDB_TURNOS_TurnPers(orden);
            for (int x=0;x<ordePerses.size();x++){
                personas.add(personaDao.load(ordePerses.get(x).getIdPersona()));
            }
            //listaOrden=personaDao._queryOrden_Persona(orden);
            this.lvPersona.setAdapter(new TurnoPersonalAdapter(rootView.getContext(), personas,FragmentPersonal.this));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            showAlertDialog(rootView.getContext(), "Error", e.toString()+String.valueOf(orden),false);

        }
    }

    //Metodo mostrar alerta
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
        int i = view.getId();
        if (i == R.id.btnNuevaPersona2) {
            formulario.setVisibility(View.VISIBLE);
        }else  if(i==R.id.btnRegresarPersona){
            formulario.setVisibility(View.GONE);
        }else if (i==R.id.btnGuardarPersona){
            String nombre=etNombreApellido.getText().toString();
            if(nombre.length()>0){


            }else{
                showAlertDialog(rootView.getContext(), "Error", "Debe especificar el nombre",false);
            }
        }
    }
}