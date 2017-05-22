package org.app.atenciondeordenes.fragment_ix_cobros;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.app.appgenesis.Globals;
import org.app.appgenesis.R;
import org.app.appgenesis.dao.Gma_costtitr;
import org.app.appgenesis.dao.Gma_costtitrDao;
import org.app.appgenesis.dao.Gro_orden;
import org.app.appgenesis.dao.Gro_ordenDao;
import org.app.appgenesis.dao.Ordecost;
import org.app.appgenesis.dao.OrdecostDao;
import org.app.atenciondeordenes.DAOApp;

import java.util.List;

import static org.app.atenciondeordenes.fragment_ix_cobros.Fragment_IX.ACTION_USUARIO;

/**
 * Created by Alexander Jimenez (alexanderenriquejm@gmail.com) on 1/5/17.
 */

public class Fragment_IXContratista extends Fragment {

    private long ordenId;
    private TextView mTotal;
    private LinearLayout linearLayout;
    private Gro_ordenDao groOrdenDao;
    private Gma_costtitrDao gmaCosttitrDao;
    private Button btnGuardar;
    private Globals globals = Globals.getInstance();
    Spinner mContratista;
    Spinner mCobro;
    View rootView;

    public Fragment_IXContratista() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ix_contratista, container, false);
        mTotal = (TextView) rootView.findViewById(R.id.tvTotalContratista);
        mContratista = (Spinner) rootView.findViewById(R.id.spContratista);
        linearLayout =(LinearLayout)rootView.findViewById(R.id.llItemContratista);
        mCobro = (Spinner) rootView.findViewById(R.id.spCobro);
        RadioGroup mRadioGroup = (RadioGroup) rootView.findViewById(R.id.rgGenerarCobroContratista);
        mRadioGroup.check(R.id.rbGcCSi);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i ==R.id.rbGcCNo){
                    Fragment_IX fragmentIx = new Fragment_IX();
                    getArguments().putBoolean("radioButtonNO",true);
                    fragmentIx.setArguments(getArguments());
                    android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.cobroContainerContratista, fragmentIx, "Fragment_IX");
                    ft.commit();

                }
            }
        });
        Spinner mResponsableSpinner = (Spinner) rootView.findViewById(R.id.spResponsableContratista);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.cobro_options, android.R.layout.simple_spinner_item);
        mResponsableSpinner.setAdapter(adapter);
        mResponsableSpinner.setSelection(2, false);
        mResponsableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == ACTION_USUARIO){
                    Fragment_IXUsuario fragmentIxUsuario = new Fragment_IXUsuario();
                    fragmentIxUsuario.setArguments(getArguments());
                    android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.cobroContainerContratista, fragmentIxUsuario, "Fragment_IXUsuario");
                    ft.commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final String[] contratistas = {"Seleccione un item","CENS", "GASES DEL ORIENTE", "EIS","AGUAS DE LOS PATIOS","MOVISTAR"};
        ArrayAdapter<String> contratistaAdapter= new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,contratistas);
        mContratista.setAdapter(contratistaAdapter);
        String[] cobros = {"Seleccione un item","SI", "NO"};
        ArrayAdapter<String> cobroAdapter= new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,cobros);
        mCobro.setAdapter(cobroAdapter);
        ordenId= getArguments().getLong("id");
        DAOApp d=new DAOApp();
        groOrdenDao = d.getGro_ordenDao();
        gmaCosttitrDao=d.getGmaCosttitrDao();
        seachCobroDB();
        btnGuardar=(Button)rootView.findViewById(R.id.btnGenerarCobroC);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cont=mContratista.getSelectedItem().toString();
                String cobr=mCobro.getSelectedItem().toString();

                if(!cont.equals("Seleccione un item")&&!cobr.equals("Seleccione un item")){
                    DAOApp daoApp=new DAOApp();
                    OrdecostDao ordecostDao=daoApp.getOrdecostDao();
                    List<Ordecost> ordecosts=ordecostDao._queryGro_orden_Ordecost(ordenId);
                    if (ordecosts.size()>0){
                        ordecosts.get(0).setUsua("Contratista");
                        ordecosts.get(0).setValor(mTotal.getText().toString());
                        if(cobr.equals("SI")){
                            ordecosts.get(0).setGene(true);
                        }else{
                            ordecosts.get(0).setGene(false);
                        }
                        ordecosts.get(0).setResp(cont);
                        ordecosts.get(0).setIdOrden(ordenId);
                        ordecosts.get(0).setSscr(globals.getUsuario_dominio());
                        ordecostDao.update(ordecosts.get(0));
                    }else {
                        Ordecost ordecost = new Ordecost();
                        ordecost.setUsua("Contratista");
                        ordecost.setValor(mTotal.getText().toString());
                        if (cobr.equals("SI")) {
                            ordecost.setGene(true);
                        } else {
                            ordecost.setGene(false);
                        }
                        ordecost.setResp(cont);
                        ordecost.setIdOrden(ordenId);
                        ordecost.setSscr(globals.getUsuario_dominio());
                        ordecostDao.insert(ordecost);
                    }
                    showAlertDialog(rootView.getContext(), "Cobro", "Registro Guardado",false);
                }else{
                    showAlertDialog(rootView.getContext(), "Error", "Debe Seleccionar una opción",false);
                }
            }
        });
        cargarCobro();
        return rootView;
    }

    private void cargarCobro(){
        DAOApp d=new DAOApp();
        OrdecostDao ordecostDao=d.getOrdecostDao();
        List<Ordecost> ordecosts=ordecostDao._queryGro_orden_Ordecost(ordenId);
        if (ordecosts.size()>0){
            switch (ordecosts.get(0).getResp()){
                case "CENS":
                    mContratista.setSelection(1);
                    break;
                case "GASES DEL ORIENTE":
                    mContratista.setSelection(2);
                    break;
                case "EIS":
                    mContratista.setSelection(3);
                    break;
                case "AGUAS DE LOS PATIOS":
                    mContratista.setSelection(4);
                    break;
                case "MOVISTAR":
                    mContratista.setSelection(5);
                    break;
            }
            if (ordecosts.get(0).getGene()){
                mCobro.setSelection(1);
            }else{
                mCobro.setSelection(2);
            }
            mTotal.setText(ordecosts.get(0).getValor());
        }
    }

    private void seachCobroDB(){
        Gro_orden groOrden = groOrdenDao.loadByRowId(ordenId);
        if(groOrden != null){
            List<Gma_costtitr> gmaCosttitrList = gmaCosttitrDao.queryBuilder()
                    .where(Gma_costtitrDao.Properties.Cstttitr.eq(groOrden.getORDETITR())).list();
            if (gmaCosttitrList.isEmpty()){

            }else{
                Double total = 0.00;
                for(int i = 0; i<= gmaCosttitrList.size()-1; i++){
                    total = total +Double.parseDouble(gmaCosttitrList.get(i).getCsttvalo());
                    LinearLayout mDynamicLayout = new LinearLayout(getActivity());
                    // Defining the LinearLayout layout parameters.
                    // In this case I want to fill its parent
                    LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    mDynamicLayout.setLayoutParams(rlp);
                    mDynamicLayout.setWeightSum(3);
                    mDynamicLayout.setOrientation(LinearLayout.HORIZONTAL);
                    mDynamicLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.gris_1));
                    TextView tipoDeTrabajo = new TextView(getActivity());
                    tipoDeTrabajo.setText("Tipo de Trabajo");
                    tipoDeTrabajo.setTypeface(Typeface.DEFAULT_BOLD,Typeface.BOLD);
                    tipoDeTrabajo.setPadding(8,8,8,8);
                    tipoDeTrabajo.setGravity(Gravity.CENTER);
                    // Defining the layout parameters of the TextView
                    LinearLayout.LayoutParams tvTT = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);

                    // Setting the parameters on the TextView
                    tipoDeTrabajo.setLayoutParams(tvTT);
                    TextView TipoDeTrabajoValor = new TextView(getActivity());
                    TipoDeTrabajoValor.setText(gmaCosttitrList.get(i).getCsttdesc()+"\nValor: "+ gmaCosttitrList.get(i).getCsttvalo());
                    TipoDeTrabajoValor.setPadding(8,8,8,8);
                    // Defining the layout parameters of the TextView
                    LinearLayout.LayoutParams tvTPV = new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,2.0f);

                    // Setting the parameters on the TextView
                    TipoDeTrabajoValor.setLayoutParams(tvTPV);
                    mDynamicLayout.addView(tipoDeTrabajo);
                    mDynamicLayout.addView(TipoDeTrabajoValor);
                    linearLayout.addView(mDynamicLayout);

                }
                mTotal.setText(""+total);


            }
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
