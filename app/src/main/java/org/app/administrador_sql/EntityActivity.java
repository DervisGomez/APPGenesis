package org.app.administrador_sql;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.app.appgenesis.R;
import org.app.atenciondeordenes.DAOApp;

public class EntityActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private EntityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity);
        DAOApp d=new DAOApp();
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView)findViewById(R.id.rvEntity);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new EntityAdapter(d.getEntities(),this);
        adapter.setOnItemClickListener(new SqlOnItemClickListener() {
            @Override
            public void onItemClicked(String nombre, Integer position) {
                Bundle bundle = new Bundle();
                bundle.putString("nombre",nombre);
                bundle.putInt("position",position);
                Intent intent = new Intent(EntityActivity.this,DataActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }
}
