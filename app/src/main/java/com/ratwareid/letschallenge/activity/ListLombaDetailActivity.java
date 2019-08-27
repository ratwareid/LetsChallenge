package com.ratwareid.letschallenge.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListLombaDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ProgressDialog loading;
    private DatabaseReference database;
    private Lomba mlomba;
    private TextView tvNama,tvTanggal,tvTempat,tvDeskripsi,tvPenyelenggara,tvJenis,tvJumlah,tvBiaya,tvTotalHadiah;
    private Button btnSimpan,btnDaftar,btnQRCode,btnListPendaftar;
    private CircleImageView civImg;
    private ArrayList<String> lombadisimpan;
    private TextView tvAnnounce;

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
    }

    public void loaddata(){

        loading = ProgressDialog.show(ListLombaDetailActivity.this,
                null,
                "Please wait...",
                true,
                false);

        String key = getIntent().getStringExtra("key");

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
                loading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
                loading.dismiss();
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
                loading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
                loading.dismiss();
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
            tvBiaya.setText(lomba.getBiaya_pendaftaran().toString());
            tvTotalHadiah.setText(lomba.getTotal_hadiah().toString());
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

            loading = ProgressDialog.show(ListLombaDetailActivity.this,
                    null,
                    "Menyimpan data...",
                    true,
                    false);

            lombadisimpan.add(mlomba.getKey());
            this.simpanData(database,lombadisimpan,"lomba_disimpan",
                    this,loading);
        }
        if (view.equals(btnDaftar)){
            loading = ProgressDialog.show(ListLombaDetailActivity.this,
                    null,
                    "Menyimpan data...",
                    true,
                    false);
            if (mlomba.getPendaftar() == null) mlomba.setPendaftar(new HashMap<String, String>());
            try {

                String combinecode = mlomba.getJenis_lomba().concat(mlomba.getKey()).concat(Constant.getLoginID());
                mlomba.getPendaftar().put(Constant.getLoginID(), IDFactory.generateUniqueKey(combinecode));
                this.daftarLomba(database,mlomba.getPendaftar(),mlomba.getKey(), this,loading);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        if (view.equals(btnQRCode)){
            showQRCode();
        }

    }

    public void simpanData(DatabaseReference database, ArrayList listid, String childDB,
                                  final Activity activity, final ProgressDialog loading) {
        database.child(childDB).child(Constant.getLoginID())
                .setValue(listid)
                .addOnSuccessListener(activity, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        loading.dismiss();
                        Toast.makeText(activity.getApplicationContext(),
                                "Data Berhasil ditambahkan",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void daftarLomba(DatabaseReference database,HashMap <String,String> listpendaftar,String idlomba, final Activity activity, final ProgressDialog loading) {
        database.child("list_lomba").child(idlomba).child("pendaftar")
                .setValue(listpendaftar)
                .addOnSuccessListener(activity, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        loading.dismiss();
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
        Bitmap bmp = ImageUtil.generateQRCODE(mlomba.getPendaftar().get(Constant.getLoginID()));
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
