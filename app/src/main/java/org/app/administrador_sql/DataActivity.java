package org.app.administrador_sql;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.app.appgenesis.R;
import org.app.atenciondeordenes.DAOApp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.AbstractDao;

public class DataActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MaterialSearchView searchView;

    private LinearLayoutManager layoutManager;
    private DataAdapter adapter;
    private String databaseName;
    private AbstractDao entity;
    private DAOApp d;
    private List<Item> dataSet2 = new ArrayList<>();
    private Integer totalItemCount;
    Boolean hasMore =true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();
        setContentView(R.layout.activity_data);
        Toolbar toolbar = (Toolbar)findViewById(R.id.tbData);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        databaseName = getIntent().getExtras().getString("nombre");
        d=new DAOApp();
        entity = d.getEntityByName(databaseName);
        List list = entity.queryBuilder().limit(10).list();
        dataSet2 = convertObjectToStringList(list);

        // buildDataSet(entity);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView)findViewById(R.id.rvDatabaseData);
        mRecyclerView.setLayoutManager(layoutManager);

        adapter = new DataAdapter(dataSet2,entity.getAllColumns(),this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (hasMore && !(hasFooter())) {
                    totalItemCount = layoutManager.getItemCount();
                    //position starts at 0
                    if (layoutManager.findLastVisibleItemPosition() == layoutManager.getItemCount() - 2) {
                        hasMore = false;
                        Log.v("...", "Last Item!");
                        dataSet2.add(new Footer());
                        recyclerView.getAdapter().notifyItemInserted(dataSet2.size() - 1);
                        List<Item> newData = getDataOffset(totalItemCount);
                        if(newData.size() >10){
                            int size = dataSet2.size();
                            dataSet2.remove(size - 1);//removes footer
                            dataSet2.addAll(newData);
                            recyclerView.getAdapter().notifyItemRangeChanged(size, dataSet2.size() - size);
                            hasMore = true;
                        }

                    }

                }
            }
        });
        searchView = (MaterialSearchView)findViewById(R.id.search_view);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                //If closed Search View , lstView will return default
                mRecyclerView = (RecyclerView) findViewById(R.id.rvDatabaseData);
                adapter = new DataAdapter(dataSet2,entity.getAllColumns(),DataActivity.this);
                mRecyclerView.setAdapter(adapter);

            }
        });
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query != null && !query.isEmpty()){
                    List<Item> lstFound;
                    entity = d.getEntityByName(databaseName);
                    if(isNumeric(query)){
                        Long id = Long.parseLong(query);
                        Object object = entity.load(id);
                        if(object != null){
                            List list = new ArrayList();
                            list.add(object);
                            lstFound = convertObjectToStringList(list);
                            adapter = new DataAdapter(lstFound,entity.getAllColumns(),DataActivity.this);
                            mRecyclerView.setAdapter(adapter);
                        }else{
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                                    DataActivity.this);

                            // set title
                            alertDialogBuilder.setTitle("Error");
                            // set dialog message
                            alertDialogBuilder
                                    .setMessage(" No se encontraron registros con id "+query)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // if this button is clicked, close
                                            // current activity
                                            dialog.cancel();
                                        }
                                    })
                            ;

                            // create alert dialog
                            android.app.AlertDialog alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();
                            return true;
                        }

                    }else{
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                                DataActivity.this);

                        // set title
                        alertDialogBuilder.setTitle("Error");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage(query+" No es un numero")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, close
                                        // current activity
                                        dialog.cancel();
                                    }
                                })
                        ;

                        // create alert dialog
                        android.app.AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                        return true;

                    }
                }
                else{
                    //if search text is null
                    //return default
                    adapter = new DataAdapter(dataSet2,entity.getAllColumns(),DataActivity.this);
                    mRecyclerView.setAdapter(adapter);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;

            }

        });
    }

    private String getMethodName(@NonNull String attributeName, @NonNull Boolean mayusculas){
        String methodName="";

        if(attributeName.contains("_")){
            String[] palabras = attributeName.split("_");
            for (int i=0; i<=palabras.length-1;i++){
                if(palabras[i].length()>0){
                    methodName = palabras[i].substring(0,1).toUpperCase() + palabras[i].substring(1).toLowerCase();
                }
            }
            methodName = "get"+methodName;
            return methodName;
        }

        if(mayusculas){
            methodName = "get"+attributeName.toUpperCase();
        }else{
            methodName  = "get"+attributeName.substring(0,1).toUpperCase()
                    + attributeName.substring(1).toLowerCase();

        }
        return methodName;
    }
    private List<Item> getDataOffset(Integer offset){
        List<Item> dataList= new ArrayList<>();
        entity = d.getEntityByName(databaseName);
        List list = entity.queryBuilder().limit(10).offset(offset).list();
        return convertObjectToStringList(list);

    }

    private List<Item> convertObjectToStringList(List objetos){
        String[] columns = entity.getAllColumns();
        Boolean mayusculas = false;
        List<Item> dataList= new ArrayList<>();

        for(int i=0; i<=objetos.size()-1;i++){
            List<String> data = new ArrayList<>();
            Class cls = objetos.get(i).getClass();
            for (int j=0; j<=columns.length-1;j++){
                Method getLabel = null;
                try {

                    getLabel = cls.getMethod(getMethodName(columns[j],mayusculas));


                } catch (NoSuchMethodException e) {
                    try {
                        mayusculas = !mayusculas;
                        getLabel = cls.getMethod(getMethodName(columns[j],mayusculas));
                    } catch (NoSuchMethodException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
                try {
                    if(getLabel != null){
                        String label = ""+getLabel.invoke(objetos.get(i));
                        data.add(label);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            dataList.add(new Data(data));
        }
        return dataList;
    }
    private boolean hasFooter() {
        Boolean hasFooter;
        try{

            hasFooter = dataSet2.get(dataSet2.size() - 1) instanceof Footer;

        }catch (Exception e){
            hasFooter = false;
        }

        return hasFooter;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
}
