package org.app.atenciondeordenes.fragment_iii_personas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.app.appgenesis.dao.Gro_ordenDao;
import org.app.appgenesis.dao.OrdePers;
import org.app.appgenesis.dao.OrdePersDao;
import org.app.appgenesis.dao.Persona;
import org.app.appgenesis.dao.PersonaDao;

import org.app.appgenesis.R;
import org.app.atenciondeordenes.DAOApp;
import org.app.atenciondeordenes.PersonaAdapter;

import java.util.ArrayList;
import java.util.List;

public class Fragment_III extends Fragment implements OnClickListener {
    private List<Persona> listaOrden=new ArrayList<Persona>();
    private long orden;
    private AutoCompleteTextView etNombreApellido;
    private Button btnRegresar;
    private Button btnGuardar;
    private Button btnNuevaPersona;
    private View rootView;
    private RelativeLayout formulario;
    private ListView lvPersona;
    private PersonaDao personaDao;
    private OrdePersDao ordePersDao;
    private String estado;

    public Fragment_III() {
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

        rootView = inflater.inflate(R.layout.fragment__iii, container, false);
        etNombreApellido=(AutoCompleteTextView)rootView.findViewById(R.id.etNombreApellido);
        btnRegresar=(Button)rootView.findViewById(R.id.btnRegresarPersona);
        btnGuardar=(Button)rootView.findViewById(R.id.btnGuardarPersona);
        btnNuevaPersona=(Button)rootView.findViewById(R.id.btnNuevaPersona2);
        formulario=(RelativeLayout) rootView.findViewById(R.id.formularioPersona);
        formulario.setVisibility(View.GONE);
        lvPersona=(ListView)rootView.findViewById(R.id.lvPersonaMostrar);

        DAOApp d=new DAOApp();
        personaDao=d.getPersonaDao();
        ordePersDao=d.getOrdePersDao();

        List<Persona> personas=new ArrayList<Persona>();
        personas=personaDao.loadAll();
        Log.d("Cantidad de Personas ->", personas.size() + "");
        //Cargar los datos de AutoCompleteTextView
        if (personas.size()>0){
            String[] auto=new String[personas.size()];
            for (int i=0;i<personas.size();i++){
                auto[i]=personas.get(i).getPersnomb();

            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(),
                    android.R.layout.simple_dropdown_item_1line, auto);
            etNombreApellido.setAdapter(adapter);
        }else{
            showAlertDialog(rootView.getContext(), "Error", "No se han descargado personas",false);
        }
        //habilata el formulario para agregar nueva persona
        btnNuevaPersona.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!estado.equals("Cerrada")){
                    if(estado.equals("Iniciada")){
                        formulario.setVisibility(View.VISIBLE);
                    }else{
                        showAlertDialog(rootView.getContext(), "Error", "Para realizar esta acción la orden debe estar en estado INICIADA",false);
                    }
                }else{
                    showAlertDialog(rootView.getContext(), "Error", "Esta orden no se puede editar porque esta CERRADA",false);
                }

            }
        });
        //Acción que agrega persona
        btnGuardar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre=etNombreApellido.getText().toString();
                //Valida que el campon nombre no este vacio
                if(nombre.length()>0){
                    try {
                        OrdePers ordePers=new OrdePers();
                        List<Persona> persona=personaDao.queryBuilder().where(PersonaDao.Properties.Persnomb.in(nombre)).list();
                        //Valida que sea un nombre valido
                        if (persona.size()>0){
                            List<OrdePers> ordePerses= ordePersDao.queryBuilder().where(OrdePersDao.Properties.IdOrden.in(orden),OrdePersDao.Properties.IdPersona.in(persona.get(0).getId())).list();
                            if(ordePerses.size()>0){
                                showAlertDialog(rootView.getContext(), "Error", "Este nombre ya esta agregado",false);
                            }else{
                                ordePers.setIdOrden(orden);
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
        //Deshabilita el formulario de nueva persona
        btnRegresar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                formulario.setVisibility(View.GONE);
                etNombreApellido.setText("");
            }
        });

        orden= getArguments().getLong("id");
        Gro_ordenDao ordenDao = d.getGro_ordenDao();
        estado =ordenDao.load(orden).getESTADO();
        buscarPersonas();

        Log.d("Framento:", "FragmentIII_load");

        return rootView;
    }
    //Metodo que carga el listado de personas asociados a la orden
    public void buscarPersonas(){
        try {
            List<Persona> personas=new ArrayList<Persona>();
            List<OrdePers> ordePerses=ordePersDao._queryGro_orden_OrdePers(orden);
            for (int x=0;x<ordePerses.size();x++){
                personas.add(personaDao.load(ordePerses.get(x).getIdPersona()));
            }
            //lama al adapter para mostrar en la interfaz el listado
            this.lvPersona.setAdapter(new PersonaAdapter(rootView.getContext(), personas,Fragment_III.this));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            showAlertDialog(rootView.getContext(), "Error", e.toString()+String.valueOf(orden),false);

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

    @Override//Metodo que escucha a que componente se le ha dado click
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