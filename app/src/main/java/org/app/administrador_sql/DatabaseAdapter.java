package org.app.administrador_sql;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.app.appgenesis.R;

import java.util.List;

/**
 * Created by Alexander Jimenez (alexanderenriquejm@gmail.com) on 1/8/17.
 */

public class DatabaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static SqlOnItemClickListener mOnItemClickLister;

    public void setOnItemClickListener(SqlOnItemClickListener listener) {

        mOnItemClickLister = listener;
    }
    private List<String> databaseList;
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
    public DatabaseAdapter(List<String> databaseList, Context mContext){
        this.databaseList = databaseList;
        this.mContext = mContext;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_databases, parent, false);
        // set the view's size, margins, paddings and layout parameters
        DatabaseViewHolder vh = new DatabaseViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(holder instanceof DatabaseViewHolder){
            ((DatabaseViewHolder) holder).getNombreDatabase().setText(databaseList.get(position));
            ((DatabaseViewHolder) holder).getNumeroDatabase().setText(""+position);
            ((DatabaseViewHolder) holder).getLinearLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickLister.onItemClicked(databaseList.get(position),position);

                }
            });

        }



    }

    public static class DatabaseViewHolder extends RecyclerView.ViewHolder {
        TextView nombreDatabase,numeroDatabase ;
        LinearLayout linearLayout;

        public DatabaseViewHolder(View itemView) {
            super(itemView);
            nombreDatabase = (TextView)itemView.findViewById(R.id.tvDatabaseName);
            numeroDatabase = (TextView)itemView.findViewById(R.id.tvNumeroDatabase);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.databaseLayout);



        }
        public TextView getNombreDatabase() {
            return nombreDatabase;
        }
        public TextView getNumeroDatabase() {
            return numeroDatabase;
        }
        public LinearLayout getLinearLayout() {
            return linearLayout;
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return databaseList.size();
    }
}
