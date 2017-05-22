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
import android.widget.TextView;

import org.app.appgenesis.dao.OrdePers;
import org.app.appgenesis.dao.OrdePersDao;
import org.app.appgenesis.dao.Persona;
import org.app.appgenesis.dao.PersonaDao;

import org.app.appgenesis.R;
import org.app.atenciondeordenes.fragment_iii_personas.Fragment_III;

import java.util.List;

public class PersonaAdapter extends BaseAdapter {

    private Context context;
    private List<Persona> items;
    private Fragment_III fragment;
    private PersonaDao personaDao;

    public PersonaAdapter(Context context, List<Persona> items,Fragment_III fragment) {
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
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_persona, parent, false);
        }

        // Set data into the view
        TextView tvId = (TextView) rowView.findViewById(R.id.tvIdPersonaMostrar);
        TextView tvNombre = (TextView) rowView.findViewById(R.id.tvNombrePersona);
        Button btnBorrar=(Button) rowView.findViewById(R.id.btnBorrarPersona);
        Button ivPersona=(Button)rowView.findViewById(R.id.ivPersona);
        DAOApp daoApp=new DAOApp();
        personaDao=daoApp.getPersonaDao();

        //mostrar datos de personal
        final Persona item = this.items.get(position);
        tvId.setText(String.valueOf(item.getPerscons()));
        tvNombre.setText(item.getPersnomb());

        //Acción borrar personal
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fragment.getEstado().equals("Cerrada")){
                    if (fragment.getEstado().equals("Iniciada")){
                        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(context);
                        dialogo1.setTitle("Importante!");
                        dialogo1.setMessage("Desea borrar este registro?");
                        dialogo1.setCancelable(false);
                        dialogo1.setPositiveButton("Acceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                DAOApp daoApp=new DAOApp();
                                OrdePersDao ordePersDao=daoApp.getOrdePersDao();
                                List<OrdePers>  ordePers= ordePersDao.queryBuilder().where(OrdePersDao.Properties.IdPersona.in(item.getId()),OrdePersDao.Properties.IdOrden.in(fragment.getOrden())).list();
                                ordePersDao.delete(ordePers.get(0));
                                fragment.buscarPersonas();
                            }
                        });
                        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {

                            }
                        });
                        dialogo1.show();
                    }else {
                        showAlertDialog(context, "Error", "Para realizar esta acción la orden debe estar en estado INICIADA",false);
                    }
                }else{
                    showAlertDialog(context, "Error", "Esta orden no se puede editar porque esta CERRADA",false);
                }

            }
        });

        return rowView;
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
}
