package org.app.atenciondeordenes.fragment_ix_cobros;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.app.appgenesis.R;
import org.app.appgenesis.dao.Gma_costtitr;
import org.app.appgenesis.dao.Gma_costtitrDao;
import org.app.appgenesis.dao.Gro_orden;
import org.app.appgenesis.dao.Gro_ordenDao;
import org.app.appgenesis.dao.Ordecost;
import org.app.appgenesis.dao.OrdecostDao;
import org.app.atenciondeordenes.DAOApp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Jimenez (alexanderenriquejm@gmail.com) on 12/30/16.
 */

public class Fragment_IX extends Fragment {

    public static final int ACTION_USUARIO = 1;
    public static final int ACTION_CONTRATISTA = 2;

    private FrameLayout frameLayout;
    private LinearLayout linearLayout;
    private Gro_ordenDao groOrdenDao;
    private RadioGroup mRadioGroup;
    private  boolean guardar=false;
    private Gma_costtitrDao gmaCosttitrDao;
    private long ordenId;
    private Button btnGuardar;
    private List<Gma_costtitr> gmaCosttitrList;

    private LinearLayout mDynamicLinearLayout;
    View rootView;

    public Fragment_IX() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ix, container, false);
        mRadioGroup = (RadioGroup)rootView.findViewById(R.id.rgGenerarCobro);

        frameLayout = (FrameLayout)rootView.findViewById(R.id.cobroContainer);
        linearLayout = (LinearLayout)rootView.findViewById(R.id.generarCobroLinearLayout);
        if(getArguments().containsKey("radioButtonNO")){
            mRadioGroup.check(R.id.rbGcNo);
        }
        btnGuardar=(Button)rootView.findViewById(R.id.btnGenerarCobro);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(guardar){
                    DAOApp daoApp=new DAOApp();
                    OrdecostDao ordecostDao=daoApp.getOrdecostDao();
                    Ordecost ordecost=new Ordecost();
                    ordecost.setGene(false);
                    ordecost.setIdOrden(ordenId);
                    ordecostDao.insert(ordecost);
                    showAlertDialog(rootView.getContext(), "Cobro", "Registro Guardado",false);
                }else{
                    showAlertDialog(rootView.getContext(), "Error", "Debe Seleccionar una opción",false);
                }
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i ==R.id.rbGcSi){
                    Gro_orden groOrden;
                    groOrden = groOrdenDao.loadByRowId(ordenId);
                    if(groOrden != null) {
                        gmaCosttitrList = gmaCosttitrDao.queryBuilder()
                                .where(Gma_costtitrDao.Properties.Cstttitr.eq(groOrden.getORDETITR())).list();
                        if(gmaCosttitrList.size()>0){
                            mDynamicLinearLayout = new LinearLayout(getActivity());
                            // Defining the LinearLayout layout parameters.
                            // In this case I want to fill its parent
                            LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.FILL_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            mDynamicLinearLayout.setLayoutParams(rlp);
                            mDynamicLinearLayout.setWeightSum(3);
                            mDynamicLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                            mDynamicLinearLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.gris_1));
                            TextView responsable = new TextView(getActivity());
                            responsable.setText("Responsable: ");
                            responsable.setTypeface(Typeface.DEFAULT_BOLD,Typeface.BOLD);
                            responsable.setPadding(8,8,8,8);
                            responsable.setGravity(Gravity.CENTER);
                            // Defining the layout parameters of the TextView
                            LinearLayout.LayoutParams tvLp = new LinearLayout.LayoutParams(
                                    0,
                                    LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);

                            // Setting the parameters on the TextView
                            responsable.setLayoutParams(tvLp);
                            Spinner spinner = new Spinner(getActivity());
                            spinner.setPadding(8,8,8,8);
                            spinner.setGravity(Gravity.CENTER);
                            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                                    R.array.cobro_options, android.R.layout.simple_spinner_item);
                            spinner.setAdapter(adapter);
                            spinner.setSelection(0, false);
                            // Defining the layout parameters of the TextView
                            LinearLayout.LayoutParams spinnerLp = new LinearLayout.LayoutParams(
                                    0,
                                    LinearLayout.LayoutParams.WRAP_CONTENT,2.0f);

                            // Setting the parameters on the TextView
                            responsable.setLayoutParams(tvLp);
                            spinner.setLayoutParams(spinnerLp);
                            // Adding the TextView with description to the LinearLayout as a child
                            mDynamicLinearLayout.addView(responsable);
                            mDynamicLinearLayout.addView(spinner);
                            linearLayout.addView(mDynamicLinearLayout);

                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if(i == ACTION_USUARIO){
                                        Fragment_IXUsuario fragmentIxUsuario = new Fragment_IXUsuario();
                                        fragmentIxUsuario.setArguments(getArguments());
                                        android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                                        ft.replace(R.id.cobroContainer, fragmentIxUsuario, "Fragment_IXUsuario");
                                        ft.commit();
                                    }
                                    if(i == ACTION_CONTRATISTA){

                                        Fragment_IXContratista fragment_ixContratista= new Fragment_IXContratista();
                                        fragment_ixContratista.setArguments(getArguments());
                                        android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                                        ft.replace(R.id.cobroContainer, fragment_ixContratista, "Fragment_IXContratista");
                                        ft.commit();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        }else{
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                                    getActivity());

                            // set title
                            alertDialogBuilder.setTitle("Error");
                            // set dialog message
                            alertDialogBuilder
                                    .setMessage("No se encontraron trabajos para esta orden")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // if this button is clicked, close
                                            // current activity
                                            dialog.cancel();
                                        }
                                    })
                            ;
                            // create alert dialog
                            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                            // show it
                            alertDialog.show();
                            mRadioGroup.clearCheck();

                        }
                    }else{
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                                getActivity());

                        // set title
                        alertDialogBuilder.setTitle("Error");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("No se encontraron trabajos para esta orden")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        dialog.cancel();
                                    }
                                })
                        ;
                        // create alert dialog
                        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();
                        mRadioGroup.clearCheck();
                    }

                }
                if (i == R.id.rbGcNo){
                    if(mDynamicLinearLayout != null)
                    linearLayout.removeView(mDynamicLinearLayout);

                }
            }
        });
        ordenId= getArguments().getLong("id");
        DAOApp d=new DAOApp();
        groOrdenDao = d.getGro_ordenDao();
        gmaCosttitrDao=d.getGmaCosttitrDao();
        seachCobroDB();
        cargarCombro();
        Log.d("Framento:", "FragmentIX_load");
        return rootView;
    }

    private void cargarCombro(){
        DAOApp d=new DAOApp();
        OrdecostDao ordecostDao=d.getOrdecostDao();
        List<Ordecost> ordecosts=ordecostDao._queryGro_orden_Ordecost(ordenId);
        if (ordecosts.size()>0){
            if (ordecosts.get(0).getUsua().equals("Contratista")){
                Fragment_IXContratista fragment_ixContratista= new Fragment_IXContratista();
                fragment_ixContratista.setArguments(getArguments());
                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.cobroContainer, fragment_ixContratista, "Fragment_IXContratista");
                ft.commit();
            }else {
                Fragment_IXUsuario fragmentIxUsuario = new Fragment_IXUsuario();
                fragmentIxUsuario.setArguments(getArguments());
                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.cobroContainer, fragmentIxUsuario, "Fragment_IXUsuario");
                ft.commit();
            }
        }
    }

    private void seachCobroDB(){

        if(gmaCosttitrDao.count() == 0){
            String json =
                    "    [{\"CSTTCONS\": 3,\n" +
                            "    \"CSTTTITR\": 10,\n" +
                            "    \"CSTTNOMB\": \"Manguera y accesorios - promedio\",\n" +
                            "    \"CSTTDESC\": \"Manguera y accesorios - promedio\",\n" +
                            "    \"CSTTVALO\": \"20000\",\n" +
                            "    \"CSTTFEIN\": \"2017-01-04 00:00:00\",\n" +
                            "    \"CSTTFEFI\": \"(null)\",\n" +
                            "    \"CSTTESTA\": \"2\",\n" +
                            "    \"CSTTUSCR\": \"wilfred.garcia\",\n" +
                            "    \"CSTTFECR\": \"2017-01-04 00:00:00\"\n" +
                            "  }," +
                            "    {\"CSTTCONS\": 1,\n" +
                            "    \"CSTTTITR\": 10,\n" +
                            "    \"CSTTNOMB\": \"Valor M/O Reparación con rotura\",\n" +
                            "    \"CSTTDESC\": \"Valor M/O Reparación con rotura\",\n" +
                            "    \"CSTTVALO\": \"100000.39\",\n" +
                            "    \"CSTTFEIN\": \"2017-01-04 00:00:00\",\n" +
                            "    \"CSTTFEFI\": \"(null)\",\n" +
                            "    \"CSTTESTA\": \"2\",\n" +
                            "    \"CSTTUSCR\": \"wilfred.garcia\",\n" +
                            "    \"CSTTFECR\": \"2017-01-04 00:00:00\"\n" +
                            "  },"+
                            "    {\"CSTTCONS\": 2,\n" +
                            "    \"CSTTTITR\": 10,\n" +
                            "    \"CSTTNOMB\": \"Valor Promedio Concreto o asfalto\",\n" +
                            "    \"CSTTDESC\": \"Valor Promedio Concreto o asfalto\",\n" +
                            "    \"CSTTVALO\": \"36000.51\",\n" +
                            "    \"CSTTFEIN\": \"2017-01-04 00:00:00\",\n" +
                            "    \"CSTTFEFI\": \"(null)\",\n" +
                            "    \"CSTTESTA\": \"2\",\n" +
                            "    \"CSTTUSCR\": \"wilfred.garcia\",\n" +
                            "    \"CSTTFECR\": \"2017-01-04 00:00:00\"\n" +
                            "  },"+
                            "    {\"CSTTCONS\": 4,\n" +
                            "    \"CSTTTITR\": 11,\n" +
                            "    \"CSTTNOMB\": \"M/O Reparación\",\n" +
                            "    \"CSTTDESC\": \"M/O Reparación\",\n" +
                            "    \"CSTTVALO\": \"30000.01\",\n" +
                            "    \"CSTTFEIN\": \"2017-01-04 00:00:00\",\n" +
                            "    \"CSTTFEFI\": \"(null)\",\n" +
                            "    \"CSTTESTA\": \"2\",\n" +
                            "    \"CSTTUSCR\": \"wilfred.garcia\",\n" +
                            "    \"CSTTFECR\": \"2017-01-04 00:00:00\"\n" +
                            "  },"+
                            "    {\"CSTTCONS\": 5,\n" +
                            "    \"CSTTTITR\": 11,\n" +
                            "    \"CSTTNOMB\": \"Costo del Servicio por TITR\",\n" +
                            "    \"CSTTDESC\": \"Costo del Servicio por TITR\",\n" +
                            "    \"CSTTVALO\": \"25001.25\",\n" +
                            "    \"CSTTFEIN\": \"2017-01-04 00:00:00\",\n" +
                            "    \"CSTTFEFI\": \"(null)\",\n" +
                            "    \"CSTTESTA\": \"2\",\n" +
                            "    \"CSTTUSCR\": \"wilfred.garcia\",\n" +
                            "    \"CSTTFECR\": \"2017-01-04 00:00:00\"\n" +
                            "  }"+

                            "]";
            try {
                JSONArray jsonArray = new JSONArray(json);
                List<Gma_costtitr> gmaCosttitrList=new ArrayList<>();
                for (int i=0; i<=jsonArray.length()-1;i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Gma_costtitr gmaCosttitr = new Gma_costtitr();
                    gmaCosttitr.setCsttcons(jsonObject.getLong("CSTTCONS"));
                    gmaCosttitr.setCstttitr(jsonObject.getLong("CSTTTITR"));
                    gmaCosttitr.setCsttnomb(jsonObject.getString("CSTTNOMB"));
                    gmaCosttitr.setCsttdesc(jsonObject.getString("CSTTDESC"));
                    gmaCosttitr.setCsttvalo(jsonObject.getString("CSTTVALO"));
                    gmaCosttitr.setCsttfein(jsonObject.getString("CSTTFEIN"));
                    gmaCosttitr.setCsttfefi(jsonObject.getString("CSTTFEFI"));
                    gmaCosttitr.setCsttesta(jsonObject.getLong("CSTTESTA"));
                    gmaCosttitr.setCsttuscr(jsonObject.getString("CSTTUSCR"));
                    gmaCosttitr.setCsttfecr(jsonObject.getString("CSTTFECR"));
                    gmaCosttitrList.add(gmaCosttitr);
                }
                gmaCosttitrDao.insertInTx(gmaCosttitrList);


            } catch (JSONException e) {
                e.printStackTrace();
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
