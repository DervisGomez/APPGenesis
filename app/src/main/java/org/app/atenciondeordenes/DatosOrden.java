package org.app.atenciondeordenes;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import org.app.appgenesis.Globals;
import org.app.appgenesis.R;
import org.app.atenciondeordenes.fragment_i_detalle.Fragment_I;
import org.app.atenciondeordenes.fragment_ii_atributos.Fragment_II;
import org.app.atenciondeordenes.fragment_iii_personas.Fragment_III;
import org.app.atenciondeordenes.fragment_iv_materiales.Fragment_IV;
import org.app.atenciondeordenes.fragment_ix_cobros.Fragment_IX;
import org.app.atenciondeordenes.fragment_v_firmas.Fragment_V;
import org.app.atenciondeordenes.fragment_vi_comentarios.Fragment_VI;
import org.app.atenciondeordenes.fragment_viii_fotografias.ControlFotografia;
import org.app.atenciondeordenes.fragment_viii_fotografias.Fragment_VIII;

import java.io.ByteArrayOutputStream;

public class DatosOrden extends AppCompatActivity implements ActionBar.TabListener,OnClickListener {

    private static long orden;
    private static long titr;
    private static byte[] byteArray;
    private static int cc=0;
    private ControlFotografia controlFotografia = ControlFotografia.getInstance();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_orden);
        iniciar();
    }

    public void iniciar(){

        Bundle bolsa=getIntent().getExtras();
        orden=bolsa.getLong("id");
        titr= Long.parseLong(bolsa.getString("titr"));

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        mViewPager
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(actionBar.newTab()
                    .setText(mSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }
    }

    @Override//recargar activity al regresar a ella
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Globals g=Globals.getInstance();
        if(g.getAccess_token()==null){
            finish();
        }

        iniciar();
        switch (controlFotografia.getControlPrueba()) {
            case 0:
                mViewPager.setCurrentItem(4);
                break;
            case 1: {
                if (resultCode == RESULT_OK) {
                    Bitmap mImageBitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byteArray = stream.toByteArray();
                    controlFotografia.setImagen(byteArray);
                }
                if(resultCode == RESULT_CANCELED){
                    controlFotografia.setControlPrueba(-1);
                }
                mViewPager.setCurrentItem(7);
                break;
            }
            case 2:
                if(data!=null){
                    Toast.makeText(DatosOrden.this, data.toString(), Toast.LENGTH_SHORT).show();
                    controlFotografia.setUri(data.getData());
                }else{
                    controlFotografia.setControlPrueba(-1);
                }
                mViewPager.setCurrentItem(7);
                break;
        } // switch

    }





    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onClick(View view) {
        showAlertDialog(this, "Error", "hola",false);

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();

            Fragment fra=null;
            switch (sectionNumber) {
                case 1:
                    fra= new Fragment_I();
                    break;
                case 2:
                    fra= new Fragment_II();
                    break;
                case 3:
                    fra= new Fragment_III();
                    break;
                case 4:
                    fra= new Fragment_IV();
                    break;
                case 5:
                    fra= new Fragment_V();
                    break;
                case 6:
                    fra= new Fragment_VI();
                    break;
                case 7:
                    fra= new Fragment_III();
                    break;
                case 8:
                    fra= new Fragment_VIII();
                    break;
                case 9:
                    fra= new Fragment_IX();
                    break;
            }

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putLong("id", orden);
            args.putLong("titr", titr);
            fra.setArguments(args);
            fragment.setArguments(args);
            return fra;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_datos_orden, container, false);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 9;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Detalle";
                case 1:
                    return "Atributo";
                case 2:
                    return "Persona";
                case 3:
                    return "Materiales";
                case 4:
                    return "Firma";
                case 5:
                    return "Comentario";
                case 6:
                    return "Ubicación";
                case 7:
                    return "Fotografias";
                case 8:
                    return "Cobro";

            }
            return null;
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
