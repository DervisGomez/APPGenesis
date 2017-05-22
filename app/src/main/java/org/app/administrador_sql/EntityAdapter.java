package org.app.administrador_sql;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.app.appgenesis.R;
import org.app.atenciondeordenes.DAOApp;

import java.util.List;

import de.greenrobot.dao.AbstractDao;

/**
 * Created by Alexander Jimenez (alexanderenriquejm@gmail.com) on 1/8/17.
 */

public class EntityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static SqlOnItemClickListener mOnItemClickLister;

    public void setOnItemClickListener(SqlOnItemClickListener listener) {

        mOnItemClickLister = listener;
    }
    private DAOApp daoApp= new DAOApp();
    private List<String> entityList;
    private Context mContext;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ViewHolder(View v) {
            super(v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EntityAdapter(List<String> entityList, Context mContext){
        this.entityList = entityList;
        this.mContext = mContext;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_entity, parent, false);
        // set the view's size, margins, paddings and layout parameters
        EntityViewHolder vh = new EntityViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(holder instanceof EntityViewHolder){
            EntityViewHolder entityViewHolder = ((EntityViewHolder) holder);
            entityViewHolder.getNumeroColumnas().setText("");
            entityViewHolder.getNumeroRegistros().setText("");
            entityViewHolder.getNumeroEntity().setText("");
            entityViewHolder.getNumeroEntity().setText("");
            AbstractDao dao = daoApp.getEntityByName(entityList.get(position));
            entityViewHolder.getNombreEntity().setText(entityList.get(position));
            entityViewHolder.getNumeroEntity().setText(""+position);
            entityViewHolder.getLinearLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickLister.onItemClicked(entityList.get(position),position);
                }
            });
            entityViewHolder.getNumeroColumnas().setText(""+dao.getAllColumns().length);
            entityViewHolder.getNumeroRegistros().setText(""+dao.count());
            entityViewHolder.getLinearLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickLister.onItemClicked(entityList.get(position),position);

                }
            });

        }



    }

    public static class EntityViewHolder extends RecyclerView.ViewHolder {
        TextView nombreEntity,numeroEntity,numeroColumnas,numeroRegistros;
        LinearLayout linearLayout;



        public EntityViewHolder(View itemView) {
            super(itemView);
            nombreEntity = (TextView)itemView.findViewById(R.id.tvEntityName);
            numeroEntity = (TextView)itemView.findViewById(R.id.tvNumeroEntity);
            numeroColumnas = (TextView)itemView.findViewById(R.id.tvnumeroColumnas);
            numeroRegistros = (TextView)itemView.findViewById(R.id.tvNumeroRegistros);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.entityLayout);



        }
        public TextView getNombreEntity() {
            return nombreEntity;
        }
        public TextView getNumeroEntity() {
            return numeroEntity;
        }
        public LinearLayout getLinearLayout() {
            return linearLayout;
        }
        public TextView getNumeroColumnas() {
            return numeroColumnas;
        }

        public TextView getNumeroRegistros() {
            return numeroRegistros;
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return entityList.size();
    }
}
