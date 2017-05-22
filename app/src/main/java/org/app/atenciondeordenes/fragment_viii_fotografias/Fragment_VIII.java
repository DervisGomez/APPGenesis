package org.app.atenciondeordenes.fragment_viii_fotografias;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.app.appgenesis.R;
import org.app.appgenesis.dao.Fotografia;
import org.app.appgenesis.dao.FotografiaDao;
import org.app.appgenesis.dao.Gro_ordenDao;
import org.app.atenciondeordenes.DAOApp;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Alexander Jimenez (alexanderenriquejm@gmail.com) on 12/29/16.
 */

public class Fragment_VIII extends Fragment {

    private static final int CAMERA_REQUEST = 1888;
    private static final int GALLERY_REQUES = 2888;

    public static final int CAMERA_BUTTON = 1;
    public static final int OPTIONS_BUTTON = 2;
    public static final int DELETE_BUTTON = 3;
    public static final int DIALOG_CLOSED_NEW = 4;
    public static final int DIALOG_CLOSED = 5;
    public static final int PHOTO_CANCELD = 6;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";

    private FotografiaDao fotografiaDao;
    private long ordenId;
    private List<Fotografia> listFotografias=new ArrayList<>();

    private Fotografia mCurrentPhoto;
    private int photoNumber;
    private int cursor;
    private Date photoDate;
    private PhotoAdapter adapter;
    private ListView mListView;
    private Bitmap mImageBitmap;
    private Boolean newItem;
    private ProgressDialog progressDialog;
    private MyAsyncTask myAsyncTask;
    private ControlFotografia controlFotografia=ControlFotografia.getInstance();
    private byte[] byteArray;
    private String estado;
    private long orden;
    View rootView;
    public Fragment_VIII() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_viii, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listview);
        progressDialog = new ProgressDialog(getActivity());

        Button picBtn = (Button) rootView.findViewById(R.id.btnNuevaFoto);


        picBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(estado.equals("Iniciada")){
                    newItem = true;
                    takePictureDialog();
                }else{
                    showAlertDialog(rootView.getContext(), "Error", "Para realizar esta acción la orden debe estar en estado INICIADA",false);
                }

            }
        });


        ordenId= getArguments().getLong("id");
        if(controlFotografia.getControlPrueba()==1){
            newItem = true;
            byteArray = controlFotografia.getImagen();
            myAsyncTask = new MyAsyncTask();
            myAsyncTask.execute();
            controlFotografia.setControlPrueba(-1);
        }if(controlFotografia.getControlPrueba()==2){
            Uri selectedImage = controlFotografia.getUri();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            newItem = true;

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            mImageBitmap = BitmapFactory.decodeFile(picturePath);
            mImageBitmap = getResizedBitmap(mImageBitmap,600,400);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            mImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
            myAsyncTask = new MyAsyncTask();
            myAsyncTask.execute();
            controlFotografia.setControlPrueba(-1);
        }else{
            DAOApp d=new DAOApp();
            fotografiaDao=d.getFotografiaDao();
            seachPhotoDB();
        }
        DAOApp d=new DAOApp();
        Log.d("Framento:", "FragmentVIII_load");
        Gro_ordenDao ordenDao =  d.getGro_ordenDao();
        estado =ordenDao.load(ordenId).getESTADO();

        return rootView;
    }

    private void seachPhotoDB(){
        //try {

        listFotografias = fotografiaDao._queryGro_orden_Fotografia(ordenId);
        adapter = new PhotoAdapter(listFotografias, getActivity());
        adapter.setOnItemClickListener(onItemClickListener);
        mListView.setAdapter(adapter);
        photoNumber = listFotografias.size();
        /*}catch (Exception e) {
            // TODO Auto-generated catch block
            showAlertDialog(rootView.getContext(), "Error", e.toString(),false);
        }*/
    }

    void takePictureDialog(){
        if(photoNumber !=3 || !newItem){
            if(newItem){
                photoNumber = photoNumber+1;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Seleccione una opcion");
            builder.setItems(R.array.camera_options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(i+1 == FragmentDialogOpcionFoto.ACTION_TAKE_PHOTO){

                        controlFotografia.setControlPrueba(1);
                        dispatchTakePictureIntent(CAMERA_REQUEST);

                    }
                    if(i+1 == FragmentDialogOpcionFoto.CHOOSE_FROM_GALLERY){
                        controlFotografia.setControlPrueba(2);
                        pickImage();
                    }
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();


        }else{

            Toast.makeText(getActivity(), "Numero Maximo de Fotos(3) Alcanzado", Toast.LENGTH_SHORT).show();
        }

    }


    void handleFragmentDialog(Fotografia fotografia){

        /*FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentDialogOpcionFoto fragmentOpcionFoto = new FragmentDialogOpcionFoto();
        Bundle bundle = new Bundle();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();*/
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentDialogOpcionFoto fragmentOpcionFoto = new FragmentDialogOpcionFoto();
        Bundle bundle = new Bundle();

        bundle.putByteArray("foto",byteArray);
        if(!newItem){
            bundle.putString("descripcion",fotografia.getDescripcion());
            bundle.putLong("fecha",fotografia.getFecha());
            bundle.putLong("id_foto",fotografia.getId());
            bundle.putLong("id_orden",fotografia.getIdOrden());

        }else{
            bundle.putLong("id_orden",ordenId);
        }
        bundle.putBoolean("newItem",newItem);
        fragmentOpcionFoto.setArguments(bundle);
        fragmentOpcionFoto.setOnItemClickListener(onItemClickListener);
        fragmentOpcionFoto.show(fm,"FragmentOpcionFoto");
    }

    OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClicked(Fotografia fotografia, Integer constant, Integer position) {
            switch(constant){
                case CAMERA_BUTTON:
                    cursor = position;
                    mCurrentPhoto = fotografia;
                    newItem = false;
                    takePictureDialog();
                    break;
                case OPTIONS_BUTTON:
                    mCurrentPhoto = fotografia;
                    newItem = false;
                    cursor = position;
                    byteArray = Base64.decode(fotografia.getFoto(), Base64.DEFAULT);
                    mImageBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute();
                    break;
                case DELETE_BUTTON:
                    photoNumber = photoNumber-1;
                    fotografiaDao.delete(fotografia);
                    seachPhotoDB();
                    cursor = position;
                    break;
                case DIALOG_CLOSED:
                    fotografiaDao.update(fotografia);
                    //listFotografias.set(cursor,fotografia);
                    seachPhotoDB();
                    adapter.notifyDataSetChanged();
                    break;
                case DIALOG_CLOSED_NEW:
                    fotografiaDao.insert(fotografia);
                    //listFotografias.add(fotografia);
                    seachPhotoDB();
                    adapter.notifyDataSetChanged();
                    break;

                case PHOTO_CANCELD:

                    if(newItem){
                        photoNumber = photoNumber-1;

                    }
                    break;
            }
        }
    };

    private void dispatchTakePictureIntent(int actionCode) {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, actionCode);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        myAsyncTask = new MyAsyncTask();
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (resultCode == RESULT_OK) {
                    mImageBitmap = (Bitmap) data.getExtras().get("data");
                    //handleBigCameraPhoto();
                    myAsyncTask.execute();


                }
                if(resultCode == RESULT_CANCELED){
                    photoNumber = photoNumber-1;
                }
                break;
            } // ACTION_TAKE_PHOTO_B

            case GALLERY_REQUES:
                if (resultCode == RESULT_OK
                        && null != data) {

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    mImageBitmap = BitmapFactory.decodeFile(picturePath);
                    mImageBitmap = getResizedBitmap(mImageBitmap,600,400);
                    myAsyncTask.execute();


                }
                if(resultCode == RESULT_CANCELED){
                    photoNumber = photoNumber-1;

                }

        } // switch


    }
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
//		alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
        alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }


    class MyAsyncTask extends AsyncTask<Void, Integer, Void> {


        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... params) {
            handleFragmentDialog(mCurrentPhoto);
            return null;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "Cargando vista previa...", "Por favor espere...");

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }

    public void pickImage() {
        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, GALLERY_REQUES);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
        outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null) );
        super.onSaveInstanceState(outState);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

}
