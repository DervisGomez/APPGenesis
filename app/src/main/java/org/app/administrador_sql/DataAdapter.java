package org.app.administrador_sql;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.app.appgenesis.R;

import java.util.List;

/**
 * Created by Alexander Jimenez (alexanderenriquejm@gmail.com) on 1/8/17.
 */

public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_DATA = 0;
    private static final int TYPE_FOOTER = 1;
    private List<Item> dataList;
    private Context mContext;
    String[] columns;

    // Provide a suitable constructor (depends on the kind of dataset)
    public DataAdapter(List<Item> dataList, String[] columns,Context mContext){
        this.dataList = dataList;
        this.mContext = mContext;
        this.columns = columns;
    }
    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position) instanceof Data) {
            return TYPE_DATA;
        } else if (dataList.get(position) instanceof Footer) {
            return TYPE_FOOTER;
        } else {
            throw new RuntimeException("ItemViewType unknown");
        }
    }
    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        if (viewType == TYPE_DATA) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_data, parent, false);
            // set the view's size, margins, paddings and layout parameters
            DataViewHolder vh = new DataViewHolder(v);
            return vh;
        }else{
            View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_footer, parent, false);
            FooterViewHolder vh = new FooterViewHolder(row);
            return vh;
        }


    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(holder instanceof DataViewHolder){
            DataViewHolder dataViewHolder =  ((DataViewHolder) holder);
            dataViewHolder.getLinearLayout().removeAllViews();
            Data data = (Data) dataList.get(position);
            for(int i = 0; i<= data.getData().size()-1; i++) {
                LinearLayout mDynamicLayout = new LinearLayout(mContext);
                // Defining the LinearLayout layout parameters.
                // In this case I want to fill its parent
                LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                mDynamicLayout.setLayoutParams(rlp);
                mDynamicLayout.setWeightSum(3);
                mDynamicLayout.setOrientation(LinearLayout.HORIZONTAL);
                mDynamicLayout.setBackgroundColor(mContext.getResources().getColor(R.color.gris_1));
                TextView tipoDeTrabajo = new TextView(mContext);
                tipoDeTrabajo.setText(columns[i]);
                tipoDeTrabajo.setTypeface(Typeface.DEFAULT_BOLD,Typeface.BOLD);
                tipoDeTrabajo.setTextColor(Color.GRAY);
                tipoDeTrabajo.setPadding(8,8,8,8);
                tipoDeTrabajo.setGravity(Gravity.CENTER);
                // Defining the layout parameters of the TextView
                LinearLayout.LayoutParams tvTT = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);

                // Setting the parameters on the TextView
                tipoDeTrabajo.setLayoutParams(tvTT);
                TextView tipoDeTrabajoValor = new TextView(mContext);
                tipoDeTrabajoValor.setText(data.getData().get(i));
                tipoDeTrabajoValor.setTextColor(Color.GRAY);
                tipoDeTrabajoValor.setPadding(8,8,8,8);
                tipoDeTrabajoValor.setFilters( new InputFilter[] { new InputFilter.LengthFilter(200) } );
                // Defining the layout parameters of the TextView
                LinearLayout.LayoutParams tvTPV = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,2.0f);

                // Setting the parameters on the TextView
                tipoDeTrabajoValor.setLayoutParams(tvTPV);
                mDynamicLayout.addView(tipoDeTrabajo);
                mDynamicLayout.addView(tipoDeTrabajoValor);
                dataViewHolder.getLinearLayout().addView(mDynamicLayout);
            }


        }



    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;

        public DataViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.dataLayout);



        }

        public LinearLayout getLinearLayout() {
            return linearLayout;
        }

    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar getProgressBar() {
            return progressBar;
        }

        private ProgressBar progressBar;

        public FooterViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.footer);
        }
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
