package org.app.atenciondeordenes.fragment_iv_materiales;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import org.app.appgenesis.dao.*;

import android.widget.TextView;

import org.app.appgenesis.R;
import org.app.appgenesis.dao.Material;
import org.app.atenciondeordenes.DAOApp;
import org.app.atenciondeordenes.MaterialAdapter;

import java.util.ArrayList;
import java.util.List;

public class Fragment_IV extends Fragment {

    private List<Material> listaOrden=new ArrayList<Material>();
    private long orden;
    private AutoCompleteTextView etNombre;
    private EditText etCantidad;
    private Button btnRegresar;
    private Button btnGuardar;
    private Button btnNuevo;
    private View rootView;
    private RelativeLayout formulario;
    private ListView lvMaterial;
    private MaterialDao materialDao;
    private OrdeMateDao ordeMateDao;
    private String estado;
    private TextView tvUnidadMedida;

    public Fragment_IV() {
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

        rootView = inflater.inflate(R.layout.fragment__iv, container, false);
        etNombre=(AutoCompleteTextView)rootView.findViewById(R.id.etNombreMaterial);
        etCantidad=(EditText)rootView.findViewById(R.id.etCantidadMaterial);
        btnRegresar=(Button)rootView.findViewById(R.id.btnRegresarMaterial);
        btnGuardar=(Button)rootView.findViewById(R.id.btnGuardarMaterial);
        btnNuevo=(Button)rootView.findViewById(R.id.btnNuevoMaterial);
        formulario=(RelativeLayout) rootView.findViewById(R.id.formularioMaterial);
        lvMaterial=(ListView) rootView.findViewById(R.id.lvMaterialMostrar);
        tvUnidadMedida=(TextView)rootView.findViewById(R.id.tvUnidadMedidaMaterial);
        formulario.setVisibility(View.GONE);
        final DAOApp daoApp=new DAOApp();
        materialDao=daoApp.getMaterialDao();

        ordeMateDao =daoApp.getOrdeMateDao();
        List<Material> materials = new ArrayList<Material>();
        materials = materialDao.loadAll();

        UnidadMedidaDao unidadMedidaDao1=daoApp.getUnidadMedidaDao();
        List<UnidadMedida> unidadMedida1=unidadMedidaDao1.loadAll();

        //Carga en la base de datos la unidades de medidas si aun no estan cargadas
        if(unidadMedida1.size() <=  0){
            cargarUnidadMedida();
        }
        Log.d("Cantidad de Mate ::: " , materials.size()+"");

        //Asocia las unidades de medida a los materiales
        if (materials.size()>0){
            String[] auto=new String[materials.size()];
            for (int i=0;i<materials.size();i++){
                auto[i]=materials.get(i).getMatedesc();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(),
                    android.R.layout.simple_dropdown_item_1line, auto);
            etNombre.setAdapter(adapter);
            etNombre.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String item=(String)adapterView.getItemAtPosition(i);
                    List<Material> materials1 = materialDao.queryBuilder().where(MaterialDao.Properties.Matedesc.in(item)).list();
                    UnidadMedidaDao unidadMedidaDao=daoApp.getUnidadMedidaDao();
                    List<UnidadMedida> unidadMedida=unidadMedidaDao.queryBuilder().where(UnidadMedidaDao.Properties.Consecutivo.in(materials1.get(0).getMateunme())).list();
                    tvUnidadMedida.setText(unidadMedida.get(0).getValor());
                }
            });
        }

        //Habilita el formulario agregar nuevo material
        btnNuevo.setOnClickListener(new View.OnClickListener() {
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

        //Acción guardar material
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre=etNombre.getText().toString();
                String cantidad=etCantidad.getText().toString();

                //valida que los campos no esten vacio
                if(nombre.length()>0&&cantidad.length()>0){
                    try {
                        OrdeMate ordeMate=new OrdeMate();
                        List<Material> materials1=materialDao.queryBuilder().where(MaterialDao.Properties.Matedesc.in(nombre)).list();
                        if (materials1.size()>0){
                            List<OrdeMate> ordeMates= ordeMateDao.queryBuilder().where(OrdeMateDao.Properties.IdOrden.in(orden),OrdeMateDao.Properties.IdMaterial.in(materials1.get(0).getId())).list();
                            if(ordeMates.size()>0){
                                showAlertDialog(rootView.getContext(), "Error", "Este nombre ya esta agregado",false);
                            }else{
                                ordeMate.setIdOrden(orden);
                                ordeMate.setIdMaterial(materials1.get(0).getId());
                                ordeMate.setCantidad(cantidad);
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

        //deshabilita el formulario de guardar nueva orden
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
        Gro_ordenDao ordenDao = daoApp.getGro_ordenDao();
        estado =ordenDao.load(orden).getESTADO();

        buscarMaterial();

        Log.d("Framento:", "FragmentIV_load");

        return rootView;
    }

    //Metodo que carga la lista de materiales
    public void buscarMaterial(){
        try {
            List<Material> materials=new ArrayList<Material>();
            List<OrdeMate> ordeMates=ordeMateDao._queryGro_orden_OrdeMate(orden);
            for (int x=0;x<ordeMates.size();x++){
                materials.add(materialDao.load(ordeMates.get(x).getIdMaterial()));
            }
            //llama al adapter para mostrar en la interfaz el listado
            this.lvMaterial.setAdapter(new MaterialAdapter(rootView.getContext(), materials,Fragment_IV.this));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            showAlertDialog(rootView.getContext(), "Error", e.toString()+String.valueOf(orden),false);

        }
    }

    //metodo que guarada las unidades de medida en la base de datos
    public void cargarUnidadMedida(){

        DAOApp daoApp= new DAOApp();
        UnidadMedidaDao unidadMedidaDao = daoApp.getUnidadMedidaDao();
        long a=1;

        ArrayList<UnidadMedida> misUnidades = new ArrayList<>();
        misUnidades.add(new UnidadMedida(a++,"113","MATERIALES - UNIDAD DE MEDIDA",""));
        misUnidades.add(new UnidadMedida(a++,"114","METRO","M"));
        misUnidades.add(new UnidadMedida(a++,"115","LITRO","LT"));
        misUnidades.add(new UnidadMedida(a++,"116","LIBRA","LB"));
        misUnidades.add(new UnidadMedida(a++,"117","CENTÍMETRO","CM"));
        misUnidades.add(new UnidadMedida(a++,"118","UNIDADES","UN"));
        misUnidades.add(new UnidadMedida(a++,"119","HORA","H"));
        misUnidades.add(new UnidadMedida(a++,"120","ROLLO","RL"));
        misUnidades.add(new UnidadMedida(a++,"121","KILO","KL"));
        misUnidades.add(new UnidadMedida(a++,"122","TUBO","TB"));
        misUnidades.add(new UnidadMedida(a++,"123","BULTO","BL"));
        misUnidades.add(new UnidadMedida(a++,"124","PAR","PA"));
        misUnidades.add(new UnidadMedida(a++,"125","METRO CÚBICO","M3"));
        misUnidades.add(new UnidadMedida(a++,"126","CUARTO","CT"));
        misUnidades.add(new UnidadMedida(a++,"127","MILILITRO","ML"));
        misUnidades.add(new UnidadMedida(a++,"128","METRO CUADRADO","M2"));
        misUnidades.add(new UnidadMedida(a++,"129","FRASCO","FC"));
        misUnidades.add(new UnidadMedida(a++,"130","GALÓN","GL"));
        misUnidades.add(new UnidadMedida(a++,"131","GRAMO","GR"));
        misUnidades.add(new UnidadMedida(a++,"132","JUEGO","JG"));
        misUnidades.add(new UnidadMedida(a++,"133","CAJA","CJ"));
        misUnidades.add(new UnidadMedida(a++,"134","RESMA","RS"));
        misUnidades.add(new UnidadMedida(a++,"135","PAQUETE","PQ"));
        misUnidades.add(new UnidadMedida(a++,"136","CANECA","CN"));
        misUnidades.add(new UnidadMedida(a++,"137","MILÍMETRO","MM"));
        misUnidades.add(new UnidadMedida(a++,"138","DÍA","DIA"));
        misUnidades.add(new UnidadMedida(a++,"139","PLIEGO","PLI"));
        misUnidades.add(new UnidadMedida(a++,"140","1/16 DE GALON","1D"));
        misUnidades.add(new UnidadMedida(a++,"141","CUÑETE","CU"));
        misUnidades.add(new UnidadMedida(a++,"142","UFC/ML/100 CM2","CAL"));

        unidadMedidaDao.insertInTx(misUnidades);

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
}