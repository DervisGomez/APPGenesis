package org.app.atenciondeturnos.verificacion;

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
import org.app.atenciondeordenes.DAOApp;
import org.app.atenciondeturnos.dao.TurnFirm;
import org.app.atenciondeturnos.dao.TurnFirmDao;

import java.util.List;

/**
 * Created by dervis on 27/11/16.
 */
public class TurnoVerificacionAdapter extends BaseAdapter {

    private Context context;
    private List<TurnFirm> items;
    private FragmentVerificacion fragment;
    private TurnFirmDao firmaDao;
    //private DibujoDao dibujoDao;
    private View rowView;

    public TurnoVerificacionAdapter(Context context, List<TurnFirm> items, FragmentVerificacion fragment) {
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
            rowView = inflater.inflate(R.layout.list_turno_verificacion, parent, false);
        }

        // Set data into the view
        TextView tvCodigo = (TextView) rowView.findViewById(R.id.tvCodigoUsuarioTurno);
        TextView tvNombre = (TextView) rowView.findViewById(R.id.tvNombreUsuarioTurno);
        TextView tvNumero = (TextView) rowView.findViewById(R.id.tvNumeroTurnoVerificacion);

        Button ver=(Button)rowView.findViewById(R.id.btnVerFirmaTurno);
        Button borrar=(Button)rowView.findViewById(R.id.btnBorrarFirmaTurno);

        //Cargar datos de verificacion
        final TurnFirm item = this.items.get(position);
        tvCodigo.setText(item.getCodigo());
        tvNombre.setText(item.getNombre());
        tvNumero.setText("Turno #"+String.valueOf(item.getIdTurno()));
        DAOApp daoApp=new DAOApp();
        firmaDao=daoApp.getTurnFirmDao();
        //dibujoDao=daoApp.getDibujoDao();

        //Accion borrar verificacion
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firmaDao.delete(item);
                fragment.buscarTurnos(fragment.getTurno());

                /*if (!fragment.getEstado().equals("Cerrada")){
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
                }*/

            }
        });

        //Accion ver detalle de verificaciion
        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.llamarVerFirma(item.getId());
                //llFirmaMostrar.setVisibility(View.VISIBLE);
            }
        });

        return rowView;
    }

    //metodo mostrar alertas
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