package org.app.atenciondeturnos.materiales;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.app.appgenesis.R;
import org.app.appgenesis.dao.UnidadMedida;
import org.app.appgenesis.dao.UnidadMedidaDao;
import org.app.atenciondeordenes.DAOApp;
import org.app.atenciondeturnos.dao.GDB_TURNOSDao;
import org.app.atenciondeturnos.dao.TurnMate;
import org.app.atenciondeturnos.dao.TurnMateDao;
import org.app.atenciondeturnos.dao.TurnMateTurn;
import org.app.atenciondeturnos.dao.TurnMateTurnDao;
import org.app.atenciondeturnos.dao.TurnUnidMedi;
import org.app.atenciondeturnos.dao.TurnUnidMediDao;

import java.util.ArrayList;
import java.util.List;

public class FragmentMateriales extends Fragment {
    private List<TurnMate> listaOrden=new ArrayList<TurnMate>();
    private long orden;
    private AutoCompleteTextView etNombre;
    private EditText etCantidad;
    private Button btnRegresar;
    private Button btnGuardar;
    private Button btnNuevo;
    private View rootView;
    private RelativeLayout formulario;
    private ListView lvMaterial;
    private TurnMateDao materialDao;
    private TurnMateTurnDao ordeMateDao;
    private String estado;
    private TextView tvUnidadMedida;

    public FragmentMateriales() {
        // Required empty public constructor
    }

    public String getEstado() {
        return estado;
    }

    public long getOrden() {
        return orden;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_turno_materiales, container, false);
        etNombre=(AutoCompleteTextView)rootView.findViewById(R.id.etNombreMaterialTurno);
        etCantidad=(EditText)rootView.findViewById(R.id.etCantidadMaterialTurno);
        btnRegresar=(Button)rootView.findViewById(R.id.btnRegresarMaterialTurno);
        btnGuardar=(Button)rootView.findViewById(R.id.btnGuardarMaterialTurno);
        btnNuevo=(Button)rootView.findViewById(R.id.btnNuevoMaterialTurno);
        formulario=(RelativeLayout) rootView.findViewById(R.id.formularioMaterialTurno);
        lvMaterial=(ListView) rootView.findViewById(R.id.lvMaterialMostrarTurno);
        tvUnidadMedida=(TextView)rootView.findViewById(R.id.tvUnidadMedidaMaterialTurno);
        formulario.setVisibility(View.GONE);
        final DAOApp daoApp=new DAOApp();
        materialDao=daoApp.getTurnMateDao();
        ordeMateDao =daoApp.getTurnMateTurnDao();
        List<TurnMate> materials = new ArrayList<TurnMate>();
        materials = materialDao.loadAll();
        TurnUnidMediDao unidadMedidaDao1=daoApp.getTurnUnidMediDao();
        List<TurnUnidMedi> unidadMedida1=unidadMedidaDao1.loadAll();
        try{
            //Cargar en la base de datos las unidades de medidas si aun no estan cargadas
            if(unidadMedida1.size()<1){
                cargarUnidadMedida();
            }
        }catch (Exception e){
            showAlertDialog(rootView.getContext(),"Error",e.toString(),false);
        }

        //Asociar las unidades de medida a los materiales
        if (materials.size()>0){
            String[] auto=new String[materials.size()];
            for (int i=0;i<materials.size();i++){
                auto[i]=materials.get(i).getMatedesc();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(),
                    android.R.layout.simple_dropdown_item_1line, auto);
            etNombre.setAdapter(adapter);

        }

        //Mostrar unidad de medida al seleccionar un material
        etNombre.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item=(String)adapterView.getItemAtPosition(i);
                List<TurnMate> materials1 = materialDao.queryBuilder().where(TurnMateDao.Properties.Matedesc.in(item)).list();
                TurnUnidMediDao unidadMedidaDao=daoApp.getTurnUnidMediDao();
                List<TurnUnidMedi> unidadMedida=unidadMedidaDao.queryBuilder().where(TurnUnidMediDao.Properties.Consecutivo.in(materials1.get(0).getMateunme())).list();
                tvUnidadMedida.setText(unidadMedida.get(0).getValor());
            }
        });

        //Accón mostrar formulario para guardar nuevo material
        btnNuevo.setOnClickListener(new View.OnClickListener() {
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

        //Acción Guardar material a la orden
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre=etNombre.getText().toString();
                String cantidad=etCantidad.getText().toString();
                if(nombre.length()>0&&cantidad.length()>0){
                    try {
                        TurnMateTurn ordeMate=new TurnMateTurn();
                        List<TurnMate> materials1=materialDao.queryBuilder().where(TurnMateDao.Properties.Matedesc.in(nombre)).list();
                        if (materials1.size()>0){
                            List<TurnMateTurn> ordeMates= ordeMateDao.queryBuilder().where(TurnMateTurnDao.Properties.IdTurno.in(orden),TurnMateTurnDao.Properties.IdMaterial.in(materials1.get(0).getId())).list();
                            if(ordeMates.size()>0){
                                showAlertDialog(rootView.getContext(), "Error", "Este nombre ya esta agregado",false);
                            }else{
                                ordeMate.setIdTurno(orden);
                                ordeMate.setIdMaterial(materials1.get(0).getId());
                                ordeMate.setCantidad(Integer.valueOf(cantidad));
                                ordeMateDao.insert(ordeMate);
                                showAlertDialog(rootView.getContext(), "Material", "Registro guardado",false);
                                buscarMaterial();
                                formulario.setVisibility(View.GONE);
                                etNombre.setText("");
                                etCantidad.setText("");
                            }
                        }else{
                            showAlertDialog(rootView.getContext(), "Error", "Nombre Invalido",false);
                        }
                    }catch (Exception e){

                    }

                }else{
                    showAlertDialog(rootView.getContext(), "Error", "Debe especificar el nombre y cantidad",false);
                }
            }
        });

        //Acción Salir del formulario guardar Material
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulario.setVisibility(View.GONE);
                etNombre.setText("");
                etCantidad.setText("");
                tvUnidadMedida.setText("");
            }
        });
        orden= getArguments().getLong("id");
        GDB_TURNOSDao ordenDao = daoApp.getGdb_turnosDao();
        //estado =ordenDao.load(orden).getESTADO();

        buscarMaterial();
        return rootView;
    }

    //Cargar listado de material
    public void buscarMaterial(){
        try {
            List<TurnMate> materials=new ArrayList<TurnMate>();
            List<TurnMateTurn> ordeMates=ordeMateDao._queryGDB_TURNOS_TurnMate(orden);
            for (int x=0;x<ordeMates.size();x++){
                materials.add(materialDao.load(ordeMates.get(x).getIdMaterial()));
            }
            //Cargar adapter para mostrar lista de materiales en la vista
            this.lvMaterial.setAdapter(new TurnoMaterialesAdapter(rootView.getContext(), materials,FragmentMateriales.this));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            showAlertDialog(rootView.getContext(), "Error", e.toString()+String.valueOf(orden),false);

        }
    }

    //metodo que guarada las unidades de medida en la base de datos
    public void cargarUnidadMedida(){

        DAOApp daoApp= new DAOApp();
        TurnUnidMediDao unidadMedidaDao = daoApp.getTurnUnidMediDao();
        long a=1;

        ArrayList<TurnUnidMedi> misUnidades = new ArrayList<>();
        misUnidades.add(new TurnUnidMedi(a++,"113","MATERIALES - UNIDAD DE MEDIDA",""));
        misUnidades.add(new TurnUnidMedi(a++,"114","METRO","M"));
        misUnidades.add(new TurnUnidMedi(a++,"115","LITRO","LT"));
        misUnidades.add(new TurnUnidMedi(a++,"116","LIBRA","LB"));
        misUnidades.add(new TurnUnidMedi(a++,"117","CENTÍMETRO","CM"));
        misUnidades.add(new TurnUnidMedi(a++,"118","UNIDADES","UN"));
        misUnidades.add(new TurnUnidMedi(a++,"119","HORA","H"));
        misUnidades.add(new TurnUnidMedi(a++,"120","ROLLO","RL"));
        misUnidades.add(new TurnUnidMedi(a++,"121","KILO","KL"));
        misUnidades.add(new TurnUnidMedi(a++,"122","TUBO","TB"));
        misUnidades.add(new TurnUnidMedi(a++,"123","BULTO","BL"));
        misUnidades.add(new TurnUnidMedi(a++,"124","PAR","PA"));
        misUnidades.add(new TurnUnidMedi(a++,"125","METRO CÚBICO","M3"));
        misUnidades.add(new TurnUnidMedi(a++,"126","CUARTO","CT"));
        misUnidades.add(new TurnUnidMedi(a++,"127","MILILITRO","ML"));
        misUnidades.add(new TurnUnidMedi(a++,"128","METRO CUADRADO","M2"));
        misUnidades.add(new TurnUnidMedi(a++,"129","FRASCO","FC"));
        misUnidades.add(new TurnUnidMedi(a++,"130","GALÓN","GL"));
        misUnidades.add(new TurnUnidMedi(a++,"131","GRAMO","GR"));
        misUnidades.add(new TurnUnidMedi(a++,"132","JUEGO","JG"));
        misUnidades.add(new TurnUnidMedi(a++,"133","CAJA","CJ"));
        misUnidades.add(new TurnUnidMedi(a++,"134","RESMA","RS"));
        misUnidades.add(new TurnUnidMedi(a++,"135","PAQUETE","PQ"));
        misUnidades.add(new TurnUnidMedi(a++,"136","CANECA","CN"));
        misUnidades.add(new TurnUnidMedi(a++,"137","MILÍMETRO","MM"));
        misUnidades.add(new TurnUnidMedi(a++,"138","DÍA","DIA"));
        misUnidades.add(new TurnUnidMedi(a++,"139","PLIEGO","PLI"));
        misUnidades.add(new TurnUnidMedi(a++,"140","1/16 DE GALON","1D"));
        misUnidades.add(new TurnUnidMedi(a++,"141","CUÑETE","CU"));
        misUnidades.add(new TurnUnidMedi(a++,"142","UFC/ML/100 CM2","CAL"));

        unidadMedidaDao.insertInTx(misUnidades);

    }

    //Metodo para mostrar alertas
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