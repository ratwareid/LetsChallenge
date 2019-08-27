package com.ratwareid.letschallenge.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratwareid.letschallenge.R;
import com.ratwareid.letschallenge.activity.HomeActivity;
import com.ratwareid.letschallenge.adapter.JenisAdapter;
import com.ratwareid.letschallenge.model.Jenis;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private DatabaseReference database;
    private ArrayList<Jenis> jenismodel;
    private RecyclerView recyclerView;
    private JenisAdapter adapter;
    private Context context;
    private HomeActivity activity;
    private ProgressBar progressBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initialize(view);
        return view;
    }

    public void initialize(View view){
        database = FirebaseDatabase.getInstance().getReference();
        recyclerView = view.findViewById(R.id.RV_Jenis);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(),4));
        }
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(),8));
        }

        activity = (HomeActivity) this.getActivity();
        context = this.getContext();
        progressBar = view.findViewById(R.id.progressbar);
    }

    public void loaddata(){


        progressBar.setVisibility(View.VISIBLE);
        database.child("jenis_lomba").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                jenismodel = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Jenis mjenis = noteDataSnapshot.getValue(Jenis.class);
                    mjenis.setKey(noteDataSnapshot.getKey());
                    jenismodel.add(mjenis);
                }

                adapter = new JenisAdapter(context, jenismodel,activity );
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