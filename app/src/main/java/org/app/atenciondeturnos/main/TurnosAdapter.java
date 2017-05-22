package org.app.atenciondeturnos.main;

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
import org.app.atenciondeturnos.dao.GDB_TURNOS;

import java.util.List;

public class TurnosAdapter extends BaseAdapter {

    private Context context;
    private List<GDB_TURNOS> items;

    public TurnosAdapter(Context context, List<GDB_TURNOS> items) {
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
            rowView = inflater.inflate(R.layout.list_turnos, parent, false);
        }

        // Set data into the view
        TextView tvId = (TextView) rowView.findViewById(R.id.tvIdTurno);
        TextView tvEstado = (TextView) rowView.findViewById(R.id.tvEstadoTurno);
        TextView tvZona=(TextView) rowView.findViewById(R.id.tvZonaTurno);
        TextView tvSector=(TextView)rowView.findViewById(R.id.tvSectorTurno);
        TextView tvDescripcion=(TextView)rowView.findViewById(R.id.tvDescripcionTurno);

        GDB_TURNOS item=this.items.get(position);

        tvId.setText(String.valueOf(item.getTURNCONS()));
        tvEstado.setText(item.getTURNESTA());
        tvZona.setText(item.getTURNZONA());
        tvSector.setText(item.getTURNSECT());
        tvDescripcion.setText(item.getTURNSEDE());

        return rowView;
    }

}
