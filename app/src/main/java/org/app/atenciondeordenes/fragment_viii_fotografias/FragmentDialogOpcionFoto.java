package org.app.atenciondeordenes.fragment_viii_fotografias;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.app.appgenesis.R;
import org.app.appgenesis.dao.Fotografia;

/**
 * Created by Alexander Jimenez (alexanderenriquejm@gmail.com) on 12/29/16.
 */

public class FragmentDialogOpcionFoto extends DialogFragment {

    private static OnItemClickListener mOnItemClickLister;

    public void setOnItemClickListener(OnItemClickListener listener) {

        mOnItemClickLister = listener;
    }


    public static final int ACTION_TAKE_PHOTO = 1;
    public static final int CHOOSE_FROM_GALLERY = 2;

    private Bitmap mImageBitmap;
    private Fotografia mCurrentPhoto;
    private ImageView ivFotografia;
    private Button btnRegresar,btnGuardar;
    private EditText mDescription;
    ProgressDialog progressDialog;

    View rootView;
    public FragmentDialogOpcionFoto() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dialog_opcion_foto, container, false);
        mImageBitmap = null;

        ivFotografia = (ImageView)rootView.findViewById(R.id.imageView2);
        btnRegresar = (Button)rootView.findViewById(R.id.btnRegresarFoto);
        btnGuardar = (Button)rootView.findViewById(R.id.btnGuardarFoto);
        btnRegresar = (Button)rootView.findViewById(R.id.btnRegresarFoto);

        mDescription = (EditText)rootView.findViewById(R.id.etDescripcion);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        byte[] byteArray = getArguments().getByteArray("foto");

        if(getArguments().getBoolean("newItem")) {

            mCurrentPhoto = new Fotografia();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            mCurrentPhoto.setFoto(encoded);
            mCurrentPhoto.setIdOrden(getArguments().getLong("id_orden"));
        }else{
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            mCurrentPhoto = new Fotografia(getArguments().getLong("id_foto"),
                    getArguments().getLong("fecha"),
                    getArguments().getString("descripcion"),
                    encoded,
                    getArguments().getLong("id_orden"));

        }
        mImageBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ivFotografia.setImageBitmap(mImageBitmap);

        if (mCurrentPhoto.getDescripcion() != null && !mCurrentPhoto.getDescripcion().isEmpty()) {
            mDescription.setText(mCurrentPhoto.getDescripcion());

        }


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Guardando Imagen");
                progressDialog.setMessage("Por favor espere...");
                progressDialog.show();
                if(!mDescription.getText().toString().trim().equals("")){
                    mCurrentPhoto.setDescripcion(mDescription.getText().toString());
                }


                if(getArguments().getBoolean("newItem")){
                    mCurrentPhoto.setFecha(System.currentTimeMillis());
                    mOnItemClickLister.onItemClicked(mCurrentPhoto,Fragment_VIII.DIALOG_CLOSED_NEW,null);


                }else{
                    mOnItemClickLister.onItemClicked(mCurrentPhoto,Fragment_VIII.DIALOG_CLOSED,null);

                }

                progressDialog.dismiss();
                dismiss();
            }
        });
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickLister.onItemClicked(mCurrentPhoto,Fragment_VIII.PHOTO_CANCELD,0);

                dismiss();
            }
        });

        return rootView;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}
