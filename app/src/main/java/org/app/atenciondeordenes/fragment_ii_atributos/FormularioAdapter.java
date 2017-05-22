package org.app.atenciondeordenes.fragment_ii_atributos;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.app.appgenesis.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Alexander Jimenez (alexanderenriquejm@gmail.com) on 12/31/16.
 */

public class FormularioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static OnFormularioClickListener mOnFormularioClickListener;

    public void setOnFormularioClickListener(OnFormularioClickListener listener) {

        mOnFormularioClickListener = listener;
    }
    private List<List<Formulario>> formularioList;
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
    public FormularioAdapter(List<List<Formulario>> formularioList, Context mContext){
        this.formularioList = formularioList;
        this.mContext = mContext;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_atributos, parent, false);
        // set the view's size, margins, paddings and layout parameters
        FormularioViewHolder vh = new FormularioViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(holder instanceof FormularioViewHolder){
            FormularioViewHolder formularioViewHolder = (FormularioViewHolder)holder;
            final List<Formulario> formularios = formularioList.get(position);
            formularioViewHolder.getTextView().setText(formularios.get(0).getGrupo().get(1));
            for(int i=0; i<=formularios.size()-1;i++ ){

                final Formulario formulario = formularios.get(i);

                // Creating a new LinearLayout

                LinearLayout dynamicLinearLayout = new LinearLayout(mContext);
                // Defining the LinearLayout layout parameters.
                // In this case I want to fill its parent
                LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                dynamicLinearLayout.setLayoutParams(rlp);
                dynamicLinearLayout.setWeightSum(3);
                dynamicLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                dynamicLinearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.gris_1));
                // Creating a new TextView
                TextView tv = new TextView(mContext);
                tv.setText(formulario.getAtridesc()+":");
                tv.setTypeface(Typeface.DEFAULT_BOLD,Typeface.BOLD);
                tv.setPadding(8,8,8,8);
                tv.setGravity(Gravity.RIGHT);
                // Defining the layout parameters of the TextView
                LinearLayout.LayoutParams tvLp = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);

                // Setting the parameters on the TextView
                tv.setLayoutParams(tvLp);

                // Adding the TextView with description to the LinearLayout as a child
                dynamicLinearLayout.addView(tv);

                // Defining the layout parameters of the Item
                LinearLayout.LayoutParams itemLp = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,2.0f);

                // Adding the Item to the LinearLayout
                switch (formulario.getCompAndroid()) {

                    case CHECK_BOX:
                        LinearLayout checkBoxLinearLayout = new LinearLayout(mContext);
                        checkBoxLinearLayout.setOrientation(LinearLayout.VERTICAL);
                        checkBoxLinearLayout.setLayoutParams(itemLp);
                        List<String> valoresCheckBox = new ArrayList<>();
                        if(formulario.getValor() == null){
                            for(int j=0;j<formulario.getValores().size();j++){
                                valoresCheckBox.add("false");
                            }

                        }else{
                            valoresCheckBox = Arrays.asList(formulario.getValor().split(","));
                        }
                        final List<Boolean> booleanCheckBox = new ArrayList<>();
                        for(int j=0;j<formulario.getValores().size();j++){
                            CheckBox checkBox= new CheckBox(mContext);
                            checkBox.setText(formulario.getValores().get(j));
                            checkBox.setPadding(8,8,8,8);
                            checkBox.setGravity(Gravity.CENTER);
                            checkBox.setChecked(Boolean.valueOf(valoresCheckBox.get(j)));
                            booleanCheckBox.add(Boolean.valueOf(valoresCheckBox.get(j)));
                            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    String valor = "";
                                    for(int j=0;j<formulario.getValores().size();j++) {
                                        if(compoundButton.getText().toString().equals(formulario.getValores().get(j))){
                                            booleanCheckBox.set(j,b);
                                        }
                                        valor = valor+booleanCheckBox.get(j)+",";
                                    }
                                    valor = valor.substring(0, valor.length()-1);

                                    formulario.setValor(valor);
                                    mOnFormularioClickListener.onItemClicked(formulario,position);

                                }
                            });
                            checkBoxLinearLayout.addView(checkBox);
                        }
                        dynamicLinearLayout.addView(checkBoxLinearLayout);
                        break;

                    case RADIO_BUTTON:
                        RadioGroup radioGroup = new RadioGroup(mContext);
                        radioGroup.setLayoutParams(itemLp);
                        int radioId = 0;
                        Boolean check = false;
                        for(int j=0;j<formulario.getValores().size();j++){
                            final RadioButton radioButton= new RadioButton(mContext);
                            radioButton.setText(formulario.getValores().get(j));
                            if(formulario.getValores().get(j).equals(formulario.getValor())){
                                radioButton.setId(radioId);
                                check = true;
                            }
                            radioButton.setPadding(8,8,8,8);
                            radioButton.setGravity(Gravity.CENTER);
                            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    if(b){
                                        formulario.setValor(radioButton.getText().toString());
                                        mOnFormularioClickListener.onItemClicked(formulario,position);
                                    }

                                }
                            });
                            radioGroup.addView(radioButton);
                            if(check)
                            radioGroup.check(radioId);
                        }


                        dynamicLinearLayout.addView(radioGroup);
                        break;

                    case SPINNER:
                        final Spinner spinner= new Spinner(mContext);
                        spinner.setLayoutParams(itemLp);
                        ArrayAdapter<String> aa= new ArrayAdapter<>(mContext,android.R.layout.simple_list_item_1,formulario.getValores());
                        spinner.setAdapter(aa);
                        for (int j=0;j<formulario.getValores().size();j++){
                            if (spinner.getItemAtPosition(j).toString().equals(formulario.getValor())){
                                spinner.setSelection(j);
                            }
                        }
                        spinner.setPadding(8,8,8,8);
                        spinner.setGravity(Gravity.CENTER);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                formulario.setValor(spinner.getItemAtPosition(i).toString());
                                mOnFormularioClickListener.onItemClicked(formulario,position);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        dynamicLinearLayout.addView(spinner);
                        break;

                    case EDIT_TEXT_AREA:
                        EditText editTextArea= new EditText(mContext);
                        editTextArea.setHint(formulario.getValores().get(0));
                        editTextArea.setLines(10);
                        editTextArea.setMinLines(5);
                        editTextArea.setGravity(Gravity.TOP);
                        editTextArea.setText(formulario.getValor());
                        editTextArea.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                formulario.setValor(charSequence.toString());
                                mOnFormularioClickListener.onItemClicked(formulario,position);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                        editTextArea.setPadding(8,8,8,8);
                        editTextArea.setGravity(Gravity.CENTER);
                        editTextArea.setFocusable(true);
                        editTextArea.setFocusableInTouchMode(true);
                        editTextArea.setLayoutParams(itemLp);
                        dynamicLinearLayout.addView(editTextArea);
                        break;

                    case EDIT_TEXT_DATE:

                        EditText editTextDate= new EditText(mContext);
                        editTextDate.setLayoutParams(itemLp);
                        editTextDate.setHint(formulario.getValores().get(0));
                        editTextDate.setInputType(4);
                        editTextDate.setText(formulario.getValor());
                        editTextDate.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                formulario.setValor(charSequence.toString());
                                mOnFormularioClickListener.onItemClicked(formulario,position);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                        editTextDate.setPadding(8,8,8,8);
                        editTextDate.setGravity(Gravity.CENTER);
                        editTextDate.setFocusable(true);
                        editTextDate.setFocusableInTouchMode(true);
                        dynamicLinearLayout.addView(editTextDate);
                        break;

                    case EDIT_TEXT_NUMERIC:
                        EditText editTextNumeric= new EditText(mContext);
                        editTextNumeric.setLayoutParams(itemLp);
                        editTextNumeric.setHint(formulario.getValores().get(0));
                        editTextNumeric.setInputType(2);
                        editTextNumeric.setText(formulario.getValor());
                        editTextNumeric.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                formulario.setValor(charSequence.toString());
                                mOnFormularioClickListener.onItemClicked(formulario,position);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                        editTextNumeric.setPadding(8,8,8,8);
                        editTextNumeric.setGravity(Gravity.CENTER);
                        editTextNumeric.setFocusable(true);
                        editTextNumeric.setFocusableInTouchMode(true);
                        dynamicLinearLayout.addView(editTextNumeric);
                        break;

                    case EDIT_TEXT_TEXT:
                        EditText editTextText= new EditText(mContext);
                        editTextText.setLayoutParams(itemLp);
                        editTextText.setHint(formulario.getValores().get(0));
                        editTextText.setInputType(1);
                        editTextText.setText(formulario.getValor());
                        editTextText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                formulario.setValor(charSequence.toString());
                                mOnFormularioClickListener.onItemClicked(formulario,position);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                        editTextText.setPadding(8,8,8,8);
                        editTextText.setGravity(Gravity.CENTER);
                        editTextText.setFocusable(true);
                        editTextText.setFocusableInTouchMode(true);
                        dynamicLinearLayout.addView(editTextText);
                        break;

                }
                // Setting the LinearLayout as our content view
                formularioViewHolder.getLinearLayout().addView(dynamicLinearLayout);
            }


        }



    }

    public static class FormularioViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout ;
        TextView textView ;

        public FormularioViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.llAtributos);
            textView = (TextView)itemView.findViewById(R.id.tvAtributosDescripcion);

        }

        public LinearLayout getLinearLayout() {
            return linearLayout;
        }

        public TextView getTextView() {
            return textView;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return formularioList.size();
    }
}

/*

 */

