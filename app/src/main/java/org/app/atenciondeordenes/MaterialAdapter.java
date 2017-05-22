package org.app.atenciondeordenes;

/**
 * Created by dervis on 09/11/16.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.app.appgenesis.dao.Material;
import org.app.appgenesis.dao.MaterialDao;

import org.app.appgenesis.R;
import org.app.appgenesis.dao.OrdeMate;
import org.app.appgenesis.dao.OrdeMateDao;
import org.app.atenciondeordenes.fragment_iv_materiales.Fragment_IV;

import java.util.List;

public class MaterialAdapter extends BaseAdapter {

    private Context context;
    private List<Material> items;
    private Fragment_IV fragment;
    private MaterialDao materialDao;

    public MaterialAdapter(Context context, List<Material> items, Fragment_IV fragment) {
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
    public View getView(int position, final View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_material, parent, false);
        }

        // Set data into the view
        TextView tvId = (TextView) rowView.findViewById(R.id.tvIdMaterialMostrar);
        TextView tvNombre = (TextView) rowView.findViewById(R.id.tvNombreMaterialMostrar);
        TextView tvCantidad = (TextView) rowView.findViewById(R.id.tvCantidadMaterialMostral);
        Button btnBorrar=(Button) rowView.findViewById(R.id.btnBorrarMaterial);
        LinearLayout llMaterial=(LinearLayout)rowView.findViewById(R.id.llMaterial);
        Button ivMaterial=(Button)rowView.findViewById(R.id.ivMaterial);
        int h=llMaterial.getHeight();
        int w=ivMaterial.getWidth();
        llMaterial.setPadding(8,(h-w)/2,8,(h-w)/2);
        DAOApp daoApp=new DAOApp();
        materialDao=daoApp.getMaterialDao();

        //mostrar datos del material
        final Material item = this.items.get(position);
        tvId.setText("ID: "+String.valueOf(item.getId()));
        tvNombre.setText(item.getMatedesc());
        OrdeMateDao ordeMateDao=daoApp.getOrdeMateDao();
        List<OrdeMate>  ordeMate= ordeMateDao.queryBuilder().where(OrdeMateDao.Properties.IdMaterial.in(item.getId()),OrdeMateDao.Properties.IdOrden.in(fragment.getOrden())).list();
        tvCantidad.setText("CANTIDAD: "+ordeMate.get(0).getCantidad());

        //Acción borrar material
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fragment.getEstado().equals("Cerrada")){
                    if(fragment.getEstado().equals("Iniciada")){
                        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(context);
                        dialogo1.setTitle("Importante!");
                        dialogo1.setMessage("Desea borrar este registro?");
                        dialogo1.setCancelable(false);
                        dialogo1.setPositiveButton("Acceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                DAOApp daoApp=new DAOApp();
                                OrdeMateDao ordeMateDao=daoApp.getOrdeMateDao();
                                List<OrdeMate>  ordeMate= ordeMateDao.queryBuilder().where(OrdeMateDao.Properties.IdMaterial.in(item.getId()),OrdeMateDao.Properties.IdOrden.in(fragment.getOrden())).list();
                                ordeMateDao.delete(ordeMate.get(0));
                                fragment.buscarMaterial();
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
