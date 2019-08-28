package com.ratwareid.letschallenge.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ratwareid.letschallenge.R;
import com.ratwareid.letschallenge.model.Jenis;

public class TambahJenis extends AppCompatActivity {

    private static final String TAG = "tamvan";
    private DatabaseReference database;

    private EditText etNama, etKode;
    private ProgressBar progressBar;
    private Button btn_cancel, btn_save;

    private String stID, stNama, stKode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_jenis);

        initialize();
    }

    private void initialize() {
        database = FirebaseDatabase.getInstance().getReference();
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        stID = getIntent().getStringExtra("id");
        stNama = getIntent().getStringExtra("nama");
        stKode = getIntent().getStringExtra("kode");

        etNama = findViewById(R.id.etNama);
        etKode = findViewById(R.id.etKode);

        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);

        etNama.setText(stNama);
        etKode.setText(stKode);
        if (stID == null || stID.equals("")){
            btn_save.setText("Save");
            btn_cancel.setText("Cancel");
        } else {
            btn_save.setText("Edit");
            btn_cancel.setText("Delete");
        }


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Snama = etNama.getText().toString();
                String Skode = etKode.getText().toString();

                if (btn_save.getText().equals("Save")){
                    // perintah save

                    if (Snama.equals("")) {
                        etNama.setError("Silahkan masukkan nama");
                        etNama.requestFocus();
                    } else if (Skode.equals("")) {
                        etKode.setError("Silahkan masukkan kode");
                        etKode.requestFocus();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        submitUser(new Jenis(
                                Snama,
                                Skode.toUpperCase(),
                                ""));

                    }
                } else {
                    // perintah edit
                    if (Snama.equals("")) {
                        etNama.setError("Silahkan masukkan nama");
                        etNama.requestFocus();
                    } else if (etKode.equals("")) {
                        etKode.setError("Silahkan masukkan kode");
                        etKode.requestFocus();
                    }else {
                        progressBar.setVisibility(View.VISIBLE);

                        editUser(new Jenis(
                                Snama,
                                Skode.toUpperCase(),
                                        "")
                                , stID);

                    }
                }

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btn_cancel.getText().equals("Cancel")) {
                    finish();
                }
            }
        });
    }


    private void submitUser(Jenis jenis) {
        database.child("jenis_lomba")
                .push()
                .setValue(jenis)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        progressBar.setVisibility(View.GONE);

                        etNama.setText("");
                        etKode.setText("");

                        Toast.makeText(TambahJenis.this,
                                "Data Berhasil ditambahkan",
                                Toast.LENGTH_SHORT).show();

                    }

                });
    }

    private void editUser(Jenis jenis, String id) {
        database.child("jenis_lomba")
                .child(id)
                .setValue(jenis)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        progressBar.setVisibility(View.GONE);

                        etNama.setText("");
                        etKode.setText("");

                        Toast.makeText(TambahJenis.this,
                                "Data Berhasil diedit",
                                Toast.LENGTH_SHORT).show();

                    }

                });
    }

}
