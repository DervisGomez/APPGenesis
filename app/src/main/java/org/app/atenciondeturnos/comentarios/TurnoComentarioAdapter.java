package org.app.atenciondeturnos.comentarios;

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
import org.app.atenciondeordenes.DAOApp;
import org.app.atenciondeordenes.fragment_vi_comentarios.Fragment_VI;
import org.app.atenciondeturnos.dao.TurnCome;
import org.app.atenciondeturnos.dao.TurnComeDao;

import java.util.List;

public class TurnoComentarioAdapter extends BaseAdapter {

    private Context context;
    private List<TurnCome> items;
    private FragmentComentarios fragment;
    private TurnComeDao comentarioDao;

    public TurnoComentarioAdapter(Context context, List<TurnCome> items, FragmentComentarios fragment) {
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
            rowView = inflater.inflate(R.layout.list_turno_comentario, parent, false);
        }

        // Set data into the view
        TextView tvTipo= (TextView) rowView.findViewById(R.id.tvTipoComentarioMostrarTurno);
        TextView tvNumero = (TextView) rowView.findViewById(R.id.tvNumeroComentarioMostrarTurno);
        TextView tvComentario = (TextView) rowView.findViewById(R.id.tvComentarioMostarTurno);
        Button btnBorrar=(Button) rowView.findViewById(R.id.btnBorrarComentarioTurno);
        DAOApp daoApp=new DAOApp();
        comentarioDao=daoApp.getTurnComeDao();
        btnBorrar.setVisibility(View.GONE);

        //Mostrar datos de comentario
        final TurnCome item = this.items.get(position);
        tvComentario.setText(item.getComedesc());
        if(!item.getComedano().equals("")){
            tvTipo.setText("TIPO: Daño");
            tvNumero.setText("TURNO #"+item.getComedano());
            btnBorrar.setVisibility(View.VISIBLE);
        }else if(!item.getComeorde().equals("")){
            tvTipo.setText("TIPO: Orden");
            tvNumero.setText("TURNO #"+item.getComeorde());

        }else if(!item.getComequej().equals("")){
            tvTipo.setText("TIPO: Queja");
            tvNumero.setText("TURNO #"+item.getComequej());
        }else if(!item.getComesoli().equals("")){
            tvTipo.setText("TIPO: Solicitud");
            tvNumero.setText("TURNO #"+item.getComesoli());
        }else if(!item.getComerevi().equals("")){
            tvTipo.setText("TIPO: Revición");
            tvNumero.setText("TURNO #"+item.getComerevi());
        }

        //Acción borrar comentario
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (!fragment.getEstado().equals("Cerrada")){
                    if(fragment.getEstado().equals("Iniciada")) {*/
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
                    /*}else{
                        showAlertDialog(context, "Error", "Para realizar esta acción la orden debe estar en estado INICIADA",false);
                    }
                }else{
                    showAlertDialog(context, "Error", "Esta orden no se puede editar porque esta CERRADA",false);
                }*/

            }
        });

        return rowView;
    }

    //Metodo mostrar alertas
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
