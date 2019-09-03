package com.ratwareid.letschallenge.activity;

//***********************************//
// Created by Jerry Erlangga         //
// My Repo: www.github.com/ratwareid //
// Email : jerryerlangga82@gmail.com //
//***********************************//


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratwareid.letschallenge.Constant;
import com.ratwareid.letschallenge.R;
import com.ratwareid.letschallenge.model.Lomba;
import com.ratwareid.letschallenge.model.Userdata;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextInputEditText etNama,etNoTlp,etAlamat,etBio;
    private ProgressBar progressBar;
    private DatabaseReference database;
    private Userdata userdata;
    private MaterialButton btnSimpan;
    private String imgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initialize();
    }

    public void initialize(){
        database = FirebaseDatabase.getInstance().getReference();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etNama = findViewById(R.id.MAT_nama);
        etNoTlp = findViewById(R.id.MAT_notlp);
        etAlamat = findViewById(R.id.MAT_alamat);
        etBio = findViewById(R.id.MAT_bio);

        btnSimpan = findViewById(R.id.btn_saveprofile);
        btnSimpan.setOnClickListener(this);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.loaddata();
    }

    public void loaddata(){
        progressBar.setVisibility(View.VISIBLE);

        database.child("userdata").child(Constant.getLoginID()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userdata = dataSnapshot.getValue(Userdata.class);
                placedata(userdata);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void placedata(Userdata data){
        if (data != null) {
            if (data.getNama() != null) {
                etNama.setText(data.getNama());
            }
            if (data.getAlamat() != null) {
                etAlamat.setText(data.getAlamat());
            }
            if (data.getNo_tlp() != null) {
                etNoTlp.setText(data.getNo_tlp());
            }
            if (data.getBio() != null) {
                etBio.setText(data.getBio());
            }
            if (data.getPhoto() != null) {
                imgProfile = data.getPhoto();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnSimpan)){
            simpanData();
        }
    }

    public Userdata getInputedData(){

        Userdata data = new Userdata();
        data.setNama(etNama.getText().toString());
        data.setAlamat(etAlamat.getText().toString());
        data.setBio(etBio.getText().toString());
        data.setNo_tlp(etNoTlp.getText().toString());
        data.setPhoto(imgProfile);
        data.setEmail(Constant.getLoginemail());
        return data;
    }

    public void simpanData(){

        userdata = getInputedData();

        database.child("userdata").child(Constant.getLoginID())
                .setValue(userdata)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),
                                "Data Berhasil disimpan",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}
