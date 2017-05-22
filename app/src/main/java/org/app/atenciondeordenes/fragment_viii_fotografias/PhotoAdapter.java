package org.app.atenciondeordenes.fragment_viii_fotografias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import org.app.appgenesis.R;
import org.app.appgenesis.dao.Fotografia;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Alexander Jimenez (alexanderenriquejm@gmail.com) on 12/29/16.
 */

public class PhotoAdapter extends BaseAdapter {
    private static OnItemClickListener mOnItemClickLister;

    public void setOnItemClickListener(OnItemClickListener listener) {

        mOnItemClickLister = listener;
    }
    private List<Fotografia> listFotografias;
    private Context mContext;
    PhotoAdapter(List<Fotografia> listFotografias, Context mContext){
        this.listFotografias = listFotografias;
        this.mContext = mContext;

    }
    @Override
    public int getCount() {
        return listFotografias.size();
    }

    @Override
    public Object getItem(int position) {
        return listFotografias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.row_fragment_viii,parent,false);

        final Fotografia fotografia = listFotografias.get(position);

        TextView id = (TextView)row.findViewById(R.id.tvFecha);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        id.setText("Fecha : "+sdf.format(new Date(fotografia.getFecha())));


        TextView descripcion = (TextView)row.findViewById(R.id.tvDescripcion);
        if (fotografia.getDescripcion() != null && !fotografia.getDescripcion().isEmpty()) {
            descripcion.setText(fotografia.getDescripcion());

        }


        ImageButton btnCamara = (ImageButton)row.findViewById(R.id.tvTomarFoto);
        ImageButton btnVerFoto = (ImageButton)row.findViewById(R.id.btnVerFoto);
        ImageButton btnBorrar = (ImageButton)row.findViewById(R.id.btnBorrarFoto);

        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickLister.onItemClicked(fotografia, Fragment_VIII.CAMERA_BUTTON,position);
            }
        });

        btnVerFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickLister.onItemClicked(fotografia,Fragment_VIII.OPTIONS_BUTTON,position);
            }
        });

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listFotografias.remove(position);
                notifyDataSetChanged();
                mOnItemClickLister.onItemClicked(fotografia,Fragment_VIII.DELETE_BUTTON,position);
            }
        });

        return row;
    }
}
