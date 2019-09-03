package com.ratwareid.letschallenge.activity;

//***********************************//
// Created by Jerry Erlangga         //
// My Repo: www.github.com/ratwareid //
// Email : jerryerlangga82@gmail.com //
//***********************************//


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ratwareid.letschallenge.R;
import com.ratwareid.letschallenge.adapter.PendaftarAdapter;
import com.ratwareid.letschallenge.model.Lomba;
import com.ratwareid.letschallenge.model.Pendaftar;
import com.ratwareid.letschallenge.model.Userdata;
import java.util.ArrayList;
import java.util.Map;

public class ListPendaftarActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private DatabaseReference database;
    private Context context;
    private String keyIntent;
    private ArrayList<Pendaftar> listpendaftar;
    private ArrayList<Userdata> listuser;
    private PendaftarAdapter adapter;
    private String textScan;
    private IntentIntegrator intentIntegrator;

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

                                listpendaftar = new ArrayList<>();
                                for (Map.Entry data : lomba.getPendaftar().entrySet()) {
                                    Pendaftar mpendaftar = (Pendaftar) data.getValue();
                                    mpendaftar.setLoginid(String.valueOf(data.getKey()));
                                    listpendaftar.add(mpendaftar);
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
        intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this, "Hasil tidak ditemukan", Toast.LENGTH_SHORT).show();
            }else{
                // jika qrcode berisi data
                textScan = result.getContents();

                if(listpendaftar == null){
                    this.loaddata();
                }

                for (int x=0;x<listpendaftar.size();x++){
                    Pendaftar pendaftar =  listpendaftar.get(x);
                    if (pendaftar.getKodependaftaran().equals(textScan)){
                        pendaftar.setfHadir("Y");
                        adapter.notifyItemChanged(x);
                        database.child("list_lomba").child(keyIntent).child("pendaftar").child(pendaftar.getLoginid()).child("fHadir").setValue("Y");
                    }
                }



            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
