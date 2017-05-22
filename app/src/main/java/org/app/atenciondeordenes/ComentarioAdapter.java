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

import org.app.appgenesis.R;
import org.app.appgenesis.dao.Comentario;
import org.app.appgenesis.dao.ComentarioDao;
import org.app.atenciondeordenes.fragment_vi_comentarios.Fragment_VI;

import java.util.List;

public class ComentarioAdapter extends BaseAdapter {

    private Context context;
    private List<Comentario> items;
    private Fragment_VI fragment;
    private ComentarioDao comentarioDao;

    public ComentarioAdapter(Context context, List<Comentario> items, Fragment_VI fragment) {
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
            rowView = inflater.inflate(R.layout.list_comentario, parent, false);
        }

        // Set data into the view
        TextView tvTipo= (TextView) rowView.findViewById(R.id.tvTipoComentarioMostrar);
        TextView tvNumero = (TextView) rowView.findViewById(R.id.tvNumeroComentarioMostrar);
        TextView tvComentario = (TextView) rowView.findViewById(R.id.tvComentarioMostar);
        Button btnBorrar=(Button) rowView.findViewById(R.id.btnBorrarComentario);
        DAOApp daoApp=new DAOApp();
        comentarioDao=daoApp.getComentarioDao();
        btnBorrar.setVisibility(View.GONE);

        final Comentario item = this.items.get(position);
        tvComentario.setText(item.getComedesc());

        //Valida el tipo de comentario
        if(!item.getComedano().equals("")){
            tvTipo.setText("TIPO: Daño");
            tvNumero.setText("ORDEN #"+item.getComedano());
        }else if(!item.getComeorde().equals("")){
            tvTipo.setText("TIPO: Orden");
            tvNumero.setText("ORDEN #"+item.getComeorde());
            btnBorrar.setVisibility(View.VISIBLE);
        }else if(!item.getComequej().equals("")){
            tvTipo.setText("TIPO: Queja");
            tvNumero.setText("ORDEN #"+item.getComequej());
        }else if(!item.getComesoli().equals("")){
            tvTipo.setText("TIPO: Solicitud");
            tvNumero.setText("ORDEN #"+item.getComesoli());
        }else if(!item.getComerevi().equals("")){
            tvTipo.setText("TIPO: Revición");
            tvNumero.setText("ORDEN #"+item.getComerevi());
        }

        //Acción borrar comentario
        btnBorrar.setOnClickListener(new View.OnClickListener() {
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
                                comentarioDao.delete(item);
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
