package com.ratwareid.letschallenge.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratwareid.letschallenge.Constant;
import com.ratwareid.letschallenge.R;
import com.ratwareid.letschallenge.adapter.JenisAdapter;
import com.ratwareid.letschallenge.adapter.LombaAdapter;
import com.ratwareid.letschallenge.model.Jenis;
import com.ratwareid.letschallenge.model.Lomba;

import java.util.ArrayList;

public class ListLombaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DatabaseReference database;
    private RecyclerView recyclerView;
    private LombaAdapter adapter;
    private Context context;
    private ListLombaActivity activity;
    private ArrayList<Lomba> listlomba;
    private String jeniskode;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_lomba);

        initialize();
    }

    public void initialize(){

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.RV_listlomba);
        context = getApplicationContext();
        activity = ListLombaActivity.this;

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
       /* jeniskode = getIntent().getStringExtra("jenis_kode");*/
        jeniskode = Constant.getTempJenis();
    }

    public void loaddata(){

        loading = ProgressDialog.show(ListLombaActivity.this,
                null,
                "Please wait...",
                true,
                false);

        database.child("list_lomba").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listlomba = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Lomba mlomba = noteDataSnapshot.getValue(Lomba.class);
                    if (mlomba.getJenis_lomba().equalsIgnoreCase(jeniskode)) {
                        mlomba.setKey(noteDataSnapshot.getKey());
                        listlomba.add(mlomba);
                    }
                }

                adapter = new LombaAdapter(context, listlomba,activity );
                recyclerView.setAdapter(adapter);
                loading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loading.dismiss();
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.loaddata();
    }
}

