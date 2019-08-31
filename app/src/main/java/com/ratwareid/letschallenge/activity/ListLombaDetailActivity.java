package com.ratwareid.letschallenge.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratwareid.letschallenge.Constant;
import com.ratwareid.letschallenge.IDFactory;
import com.ratwareid.letschallenge.ImageUtil;
import com.ratwareid.letschallenge.R;
import com.ratwareid.letschallenge.adapter.LombaAdapter;
import com.ratwareid.letschallenge.model.Lomba;
import com.ratwareid.letschallenge.model.Pendaftar;
import com.ratwareid.letschallenge.model.Userdata;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListLombaDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private DatabaseReference database;
    private Lomba mlomba;
    private TextView tvNama,tvTanggal,tvTempat,tvDeskripsi,tvPenyelenggara,tvJenis,tvJumlah,tvBiaya,tvTotalHadiah;
    private Button btnSimpan,btnDaftar,btnQRCode,btnListPendaftar;
    private CircleImageView civImg;
    private ArrayList<String> lombadisimpan;
    private TextView tvAnnounce;
    public static String key;
    private SharedPreferences prefs;
    private Userdata datapendaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_lomba_detail);
        initalize();
    }

    private void initalize() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        database = FirebaseDatabase.getInstance().getReference();

        tvNama = findViewById(R.id.TV_namalomba);
        tvTanggal = findViewById(R.id.TV_tanggallomba);
        tvTempat = findViewById(R.id.TV_tempatlomba);
        tvDeskripsi = findViewById(R.id.deskripsilomba);
        tvPenyelenggara = findViewById(R.id.penyelenggara);
        tvJenis = findViewById(R.id.namajenis);
        tvJumlah = findViewById(R.id.jumlahpeserta);
        tvBiaya = findViewById(R.id.biayapendaftaran);
        tvTotalHadiah = findViewById(R.id.totalhadiah);
        btnSimpan = findViewById(R.id.btn_simpan);
        btnSimpan.setOnClickListener(this);
        btnDaftar = findViewById(R.id.btn_daftar);
        btnDaftar.setOnClickListener(this);
        btnQRCode = findViewById(R.id.btn_qrcode);
        btnQRCode.setOnClickListener(this);
        civImg = findViewById(R.id.CIV_lomba);
        tvAnnounce = findViewById(R.id.TV_berhasildaftar);
        btnListPendaftar = findViewById(R.id.btn_showpendaftar);
        btnListPendaftar.setOnClickListener(this);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        key =  getIntent().getStringExtra("key");
        if (key != null) {
            prefs = getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("keyIntent", key);
            editor.apply();
        }
    }

    public static SharedPreferences getDefaultSharedPreferences(Context context) {
        return context.getSharedPreferences(getDefaultSharedPreferencesName(context),
                getDefaultSharedPreferencesMode());
    }

    private static String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }

    private static int getDefaultSharedPreferencesMode() {
        return Context.MODE_PRIVATE;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        prefs = getDefaultSharedPreferences(getApplicationContext());
        prefs.edit().remove("keyIntent").apply();
    }

    public void loaddata(){

        if (key == null){
            prefs = getDefaultSharedPreferences(getApplicationContext());
            key = prefs.getString("keyIntent",key);
        }

        progressBar.setVisibility(View.VISIBLE);

        database.child("list_lomba").child(key).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mlomba = dataSnapshot.getValue(Lomba.class);
                mlomba.setKey(dataSnapshot.getKey());
                placedata(mlomba);

                if (mlomba.getPenyelenggara().equals(Constant.getLoginemail())){
                    btnDaftar.setEnabled(false);
                    btnSimpan.setEnabled(false);
                    btnDaftar.setVisibility(View.GONE);
                    btnSimpan.setVisibility(View.GONE);
                    btnListPendaftar.setEnabled(true);
                    btnListPendaftar.setVisibility(View.VISIBLE);
                }else {

                    if (mlomba.getPendaftar() != null) {
                        if (mlomba.getPendaftar().containsKey(Constant.getLoginID())) {
                            btnDaftar.setVisibility(View.GONE);
                            btnDaftar.setEnabled(false);
                            btnQRCode.setEnabled(true);
                            btnQRCode.setVisibility(View.VISIBLE);
                            tvAnnounce.setText("Kamu Telah Terdaftar Pada Perlombaan Ini !");
                            tvAnnounce.setVisibility(View.VISIBLE);
                            tvAnnounce.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            tvAnnounce.setTextColor(R.color.yellow);
                        }
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });

        database.child("lomba_disimpan").child(Constant.getLoginID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lombadisimpan = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    String idLomba = noteDataSnapshot.getValue(String.class);
                    lombadisimpan.add(idLomba);
                }

                if (lombadisimpan != null && lombadisimpan.size() > 0){
                    if (lombadisimpan.contains(mlomba.getKey())){
                        btnSimpan.setEnabled(false);
                        btnSimpan.setVisibility(View.GONE);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });

        database.child("userdata").child(Constant.getLoginID()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datapendaftar = dataSnapshot.getValue(Userdata.class);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void placedata(Lomba lomba){
        if (lomba != null){

            String imgdecoded = lomba.getLogo_lomba();
            byte[] decodedString = Base64.decode(imgdecoded, Base64.DEFAULT);
            civImg.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));

            tvNama.setText(lomba.getNama_lomba());
            tvTanggal.setText(lomba.getTanggal_lomba());
            tvTempat.setText(lomba.getAlamat_lomba());
            tvDeskripsi.setText(lomba.getDeskripsi_lomba());
            tvPenyelenggara.setText(lomba.getPenyelenggara());
            tvJenis.setText(lomba.getNama_jenis());
            tvJumlah.setText(lomba.getJumlah_peserta());
            if (lomba.getBiaya_pendaftaran() != null) {
                tvBiaya.setText(lomba.getBiaya_pendaftaran().toString());
            }else{
                tvBiaya.setText("0");
            }
            if (lomba.getTotal_hadiah() != null) {
                tvTotalHadiah.setText(lomba.getTotal_hadiah().toString());
            }else{
                tvTotalHadiah.setText("0");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.loaddata();
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnSimpan)){

            progressBar.setVisibility(View.VISIBLE);

            lombadisimpan.add(mlomba.getKey());
            this.simpanData(database,lombadisimpan,"lomba_disimpan",
                    this,progressBar);
        }
        if (view.equals(btnDaftar)){
            if (datapendaftar != null) {
                progressBar.setVisibility(View.VISIBLE);
                if (mlomba.getPendaftar() == null)
                    mlomba.setPendaftar(new HashMap<String, Pendaftar>());
                try {
                    String combinecode = mlomba.getJenis_lomba().concat(mlomba.getKey()).concat(Constant.getLoginID());
                    Pendaftar pendaftar = new Pendaftar();
                    pendaftar.setEmail(Constant.getLoginemail());
                    pendaftar.setKodependaftaran(IDFactory.generateUniqueKey(combinecode));
                    mlomba.getPendaftar().put(Constant.getLoginID(),pendaftar);
                    this.daftarLomba(database, mlomba.getPendaftar(), mlomba.getKey(), this, progressBar);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(getApplicationContext(),
                        "Gagal mendaftar, mohon lengkapi data diri anda !",
                        Toast.LENGTH_SHORT).show();
            }
        }
        if (view.equals(btnQRCode)){
            showQRCode();
        }
        if (view.equals(btnListPendaftar)){

            Intent myIntent = new Intent(this, ListPendaftarActivity.class);
            myIntent.putExtra("key",key);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            this.startActivity(myIntent,options.toBundle());
        }
    }

    public void simpanData(DatabaseReference database, ArrayList listid, String childDB,
                                  final Activity activity, final ProgressBar progressBar) {
        database.child(childDB).child(Constant.getLoginID())
                .setValue(listid)
                .addOnSuccessListener(activity, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity.getApplicationContext(),
                                "Data Berhasil ditambahkan",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void daftarLomba(DatabaseReference database,HashMap pendaftar,String idlomba, final Activity activity, final ProgressBar progressBar) {
        database.child("list_lomba").child(idlomba).child("pendaftar")
                .setValue(pendaftar)
                .addOnSuccessListener(activity, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity.getApplicationContext(),
                                "Data Berhasil ditambahkan",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void showQRCode(){
        final Dialog dialogPicture = new Dialog(this);
        dialogPicture.setCancelable(true);

        View view  = this.getLayoutInflater().inflate(R.layout.dialog_qrcode, null);
        ImageView IVqrcode = view.findViewById(R.id.QRCode);
        Bitmap bmp = ImageUtil.generateQRCODE(mlomba.getPendaftar().get(Constant.getLoginID()).getKodependaftaran());
        IVqrcode.setImageBitmap(bmp);
        dialogPicture.setContentView(view);
        dialogPicture.show();
    }

    public Lomba getMlomba() {
        return mlomba;
    }

    public void setMlomba(Lomba mlomba) {
        this.mlomba = mlomba;
    }

    public ArrayList<String> getLombadisimpan() {
        return lombadisimpan;
    }

    public void setLombadisimpan(ArrayList<String> lombadisimpan) {
        this.lombadisimpan = lombadisimpan;
    }
}
