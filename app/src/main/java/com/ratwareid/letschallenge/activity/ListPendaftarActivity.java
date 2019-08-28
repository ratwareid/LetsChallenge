package com.ratwareid.letschallenge.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.ratwareid.letschallenge.R;
import com.ratwareid.letschallenge.adapter.LombaAdapter;
import com.ratwareid.letschallenge.adapter.PendaftarAdapter;
import com.ratwareid.letschallenge.model.Lomba;
import com.ratwareid.letschallenge.model.Pendaftar;
import com.ratwareid.letschallenge.model.Userdata;

import java.util.ArrayList;
import java.util.HashMap;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ListPendaftarActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private DatabaseReference database;
    private Context context;
    private String keyIntent;
    private ArrayList<Pendaftar> listpendaftar;
    private ArrayList<Userdata> listuser;
    private PendaftarAdapter adapter;
    private ZXingScannerView mScannerView;
    private String textScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pendaftar);

        this.initialize();
    }

    public void initialize(){
        context = this.getApplicationContext();
        database = FirebaseDatabase.getInstance().getReference();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.RV_listpendaftar);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);
        keyIntent = getIntent().getStringExtra("key");
        mScannerView = new ZXingScannerView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pendaftar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            /*Toast.makeText(this, "Action clicked", Toast.LENGTH_LONG).show();*/
            startScan();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.loaddata();
    }

    public void loaddata(){
        progressBar.setVisibility(View.VISIBLE);
        database.child("userdata").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listuser = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Userdata userdata = noteDataSnapshot.getValue(Userdata.class);
                    userdata.setKey(noteDataSnapshot.getKey());
                    listuser.add(userdata);
                }

                database.child("list_lomba").child(keyIntent).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Lomba lomba = dataSnapshot.getValue(Lomba.class);
                        if (lomba.getPendaftar() != null && lomba.getPendaftar().size() > 0){

                            if (listuser != null) {
                                HashMap<String, String> lombapendaftar = lomba.getPendaftar();
                                listpendaftar = new ArrayList<>();
                                for (Object data : listuser) {
                                    Userdata user = (Userdata) data;

                                    if (lombapendaftar.containsKey(user.getKey())) {
                                        Pendaftar pendaftar = new Pendaftar();
                                        pendaftar.setEmail(user.getEmail());
                                        pendaftar.setKodependaftaran(lombapendaftar.get(user.getKey()));
                                        pendaftar.setLoginid(user.getKey());
                                        listpendaftar.add(pendaftar);
                                    }
                                }

                                adapter = new PendaftarAdapter(context, listpendaftar, ListPendaftarActivity.this);
                                recyclerView.setAdapter(adapter);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setVisibility(View.INVISIBLE);
                        System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });
    }

    public void startScan(){
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Log.v("TAG", result.getText()); // Prints scan results
        Log.v("TAG", result.getBarcodeFormat().toString());

        mScannerView.stopCamera(); //<- then stop the camera
        setContentView(R.layout.activity_list_pendaftar); //<-
        this.loaddata();
        textScan = result.getText();
        if (listpendaftar != null && listpendaftar.size() > 0){
            for (int x=0; x<listpendaftar.size(); x++){
                Pendaftar pendaftar = listpendaftar.get(x);
                if (pendaftar.getKodependaftaran().equals(textScan)){
                    pendaftar.setfHadir("Y");
                    adapter.notifyItemChanged(x);
                }
            }
        }
    }
}
