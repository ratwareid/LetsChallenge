package com.ratwareid.letschallenge.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratwareid.letschallenge.Constant;
import com.ratwareid.letschallenge.R;
import com.ratwareid.letschallenge.activity.HomeActivity;
import com.ratwareid.letschallenge.activity.ListLombaActivity;
import com.ratwareid.letschallenge.adapter.LombaAdapter;
import com.ratwareid.letschallenge.model.Lomba;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {

    private DatabaseReference database;
    private RecyclerView recyclerView;
    private HomeActivity activity;
    private Context context;
    private ProgressBar progressBar;
    private ArrayList<String> listdisimpan;
    private ArrayList<Lomba> listlomba;
    private LombaAdapter adapter;
    private Toolbar toolbar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        initialize(view);
        return view;
    }

    public void initialize(View view){
        database = FirebaseDatabase.getInstance().getReference();
        activity = (HomeActivity) this.getActivity();
        context = this.getContext();
        progressBar = view.findViewById(R.id.progressbar);
        recyclerView = view.findViewById(R.id.RV_favorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(activity.getApplicationContext(), DividerItemDecoration.VERTICAL));
    }

    public void loaddata(){
        progressBar.setVisibility(View.VISIBLE);

        database.child("lomba_disimpan").child(Constant.getLoginID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listdisimpan = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String idlomba = data.getValue(String.class);
                    listdisimpan.add(idlomba);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });

        database.child("list_lomba").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listlomba = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Lomba mlomba = noteDataSnapshot.getValue(Lomba.class);
                    if (listdisimpan != null) {
                        if (listdisimpan.contains(noteDataSnapshot.getKey())) {
                            mlomba.setKey(noteDataSnapshot.getKey());
                            listlomba.add(mlomba);
                        }
                    }
                }

                adapter = new LombaAdapter(context, listlomba,activity );
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        this.loaddata();
    }
}
