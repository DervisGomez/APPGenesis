package org.app.atenciondeordenes;

/**
 * Created by dervis on 09/11/16.
 */
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.app.appgenesis.R;
import org.app.appgenesis.dao.Gro_orden;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrdenAdapter extends BaseAdapter {

    private Context context;
    private List<Gro_orden> items;

    public OrdenAdapter(Context context, List<Gro_orden> items) {
        this.context = context;
        this.items = items;
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
            rowView = inflater.inflate(R.layout.list_items, parent, false);
        }

        // Set data into the view
        TextView tvId = (TextView) rowView.findViewById(R.id.tvId);
        TextView tvSolicitud = (TextView) rowView.findViewById(R.id.tvSolicitud);
        TextView tvTipo = (TextView) rowView.findViewById(R.id.tvTipo);
        TextView tvContrato = (TextView) rowView.findViewById(R.id.tvContrato);
        TextView tvDireccion = (TextView) rowView.findViewById(R.id.tvDireccion);
        TextView tvEstado = (TextView) rowView.findViewById(R.id.tvEstado);
        TextView tvSoli=(TextView) rowView.findViewById(R.id.tvTSolicitud);
        TextView tvFecha=(TextView)rowView.findViewById(R.id.tvFecha);
        TextView tvLabelSolicitud=(TextView)rowView.findViewById(R.id.tvLabelSolicitud);

        Gro_orden item = this.items.get(position);

        //Mostrar datos de la orden
        long tmp = Long.valueOf(item.getORDEFECR());
        Date date=new Date(tmp);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateText = df2.format(date);
        tvId.setText(String.valueOf(item.getORDECONS()));
        tvSolicitud.setText(item.getCONTNOMB());
        tvTipo.setText(item.getTITRDESC());
        tvContrato.setText(item.getCONTCODI());
        tvDireccion.setText(item.getCONTDIRE()+" "+item.getVALOBARR());
        tvEstado.setText(item.getESTADO());
        tvSoli.setText(item.getORDENUDO());
        tvFecha.setText(dateText);
        tvLabelSolicitud.setText(item.getTIPODOCU());

        //validar estado de la orden
        if(item.getESTADO().equals("Iniciada")){
            tvEstado.setTextColor(Color.BLUE);
        }else if (item.getESTADO().equals("Suspendida")){
            tvEstado.setTextColor(Color.rgb(252,168,0));
        }else if (item.getESTADO().equals("Pendiente")){
            tvEstado.setTextColor(Color.rgb(142,142,142));
        }else if (item.getESTADO().equals("Cerrada")){
            tvEstado.setTextColor(Color.GREEN);
        }else if (item.getESTADO().equals("Sincronizada")){
            tvEstado.setTextColor(Color.RED);
        }

        return rowView;
    }

}
