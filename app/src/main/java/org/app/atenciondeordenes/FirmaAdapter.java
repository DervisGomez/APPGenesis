package org.app.atenciondeordenes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import org.app.appgenesis.dao.Firma;
import org.app.appgenesis.dao.FirmaDao;
import org.app.appgenesis.dao.Dibujo;
import org.app.appgenesis.dao.DibujoDao;

import org.app.appgenesis.R;
import org.app.atenciondeordenes.fragment_v_firmas.Fragment_V;

import java.util.List;

/**
 * Created by dervis on 27/11/16.
 */
public class FirmaAdapter extends BaseAdapter {

    private Context context;
    private List<Firma> items;
    private Fragment_V fragment;
    private FirmaDao firmaDao;
    private DibujoDao dibujoDao;
    private View rowView;

    public FirmaAdapter(Context context, List<Firma> items, Fragment_V fragment) {
        this.context = context;
        this.items = items;
        this.fragment=fragment;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_firma, parent, false);
        }

        // Set data into the view
        TextView tvDocumento = (TextView) rowView.findViewById(R.id.tvDocumentoFirma);
        TextView tvNombre = (TextView) rowView.findViewById(R.id.tvNombreFirma);
        TextView tvCorreo = (TextView) rowView.findViewById(R.id.tvCorreoFirma);

        Button ver=(Button)rowView.findViewById(R.id.btnVerFirma);
        Button borrar=(Button)rowView.findViewById(R.id.btnBorrarFirma);

        /*llFirmaMostrar.setVisibility(View.GONE);
        int x=llFirmaMostrar.getWidth();
        llFirmaMostrar.setMinimumHeight(x+50);*/
        final Firma item = this.items.get(position);
        tvDocumento.setText(item.getDocumento());
        tvNombre.setText(item.getNombre());
        tvCorreo.setText(item.getCorreo());
        DAOApp daoApp=new DAOApp();
        firmaDao=daoApp.getFirmaDao();
        dibujoDao=daoApp.getDibujoDao();

        //Acción borrar firma
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fragment.getEstado().equals("Cerrada")){
                    if(fragment.getEstado().equals("Iniciada")) {
                        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(context);
                        dialogo1.setTitle("Importante!");
                        dialogo1.setMessage("Desea borrar este registro?");
                        dialogo1.setCancelable(false);
                        dialogo1.setPositiveButton("Acceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                List<Dibujo> dibujos = dibujoDao._queryGro_orden_Dibujo(item.getId());
                                for (int x = 0; x < dibujos.size(); x++) {
                                    dibujoDao.delete(dibujos.get(x));
                                }
                                firmaDao.delete(item);
                                fragment.buscarOrdenes(fragment.getOrden());
                            }
                        });
                        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {

                            }
                        });
                        dialogo1.show();
                    }else{
                        showAlertDialog(context, "Error", "Para realizar esta acción la orden debe estar en estado INICIADA",false);
                    }
                }else{
                    showAlertDialog(context, "Error", "Esta orden no se puede editar porque esta CERRADA",false);
                }

            }
        });

        //Acción ver firma
        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.llamarVerFirma(item.getId());
                //llFirmaMostrar.setVisibility(View.VISIBLE);
            }
        });

        return rowView;
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