package com.ratwareid.letschallenge.activity;

//***********************************//
// Created by Jerry Erlangga         //
// My Repo: www.github.com/ratwareid //
// Email : jerryerlangga82@gmail.com //
//***********************************//


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratwareid.letschallenge.Constant;
import com.ratwareid.letschallenge.R;
import com.ratwareid.letschallenge.adapter.LombaAdapter;
import com.ratwareid.letschallenge.model.Lomba;

import java.util.ArrayList;

public class ListLombaTerdaftarActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DatabaseReference database;
    private RecyclerView recyclerView;
    private LombaAdapter adapter;
    private Context context;
    private ListLombaTerdaftarActivity activity;
    private ArrayList<Lomba> listlomba;
    private String jeniskode;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_lomba_terdaftar);

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
        activity = ListLombaTerdaftarActivity.this;

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
       /* jeniskode = getIntent().getStringExtra("jenis_kode");*/
        jeniskode = Constant.getTempJenis();
        progressBar = findViewById(R.id.progressbar);
    }

    public void loaddata(){

        progressBar.setVisibility(View.VISIBLE);

        database.child("list_lomba").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listlomba = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Lomba mlomba = noteDataSnapshot.getValue(Lomba.class);
                    if (mlomba.getPendaftar() != null) {
                        if (mlomba.getPendaftar().containsKey(Constant.getLoginID())) {
                            mlomba.setKey(noteDataSnapshot.getKey());
                            listlomba.add(mlomba);
                        }
                    }
                }

                adapter = new LombaAdapter(context, listlomba,activity );
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
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

