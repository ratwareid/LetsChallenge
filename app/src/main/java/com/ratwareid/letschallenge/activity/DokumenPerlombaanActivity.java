package com.ratwareid.letschallenge.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.ratwareid.letschallenge.IDFactory;
import com.ratwareid.letschallenge.ImageUtil;
import com.ratwareid.letschallenge.R;
import com.ratwareid.letschallenge.adapter.LombaAdapter;
import com.ratwareid.letschallenge.fragment.DatePickerFragment;
import com.ratwareid.letschallenge.model.Jenis;
import com.ratwareid.letschallenge.model.Lomba;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class DokumenPerlombaanActivity extends AppCompatActivity implements View.OnClickListener{

    private CircleImageView CIVprofile,CIVbtnadd;
    private TextInputEditText etJudul,etTanggal,etAlamat,etDeskripsi,etJmlPeserta,etBiaya,etTotalHadiah;
    private MaterialButton btnPengajuan;
    private Spinner spnJenis;
    private ArrayAdapter<String> spinneradapter;
    private ArrayList<String> listJenis;
    private HashMap<String,String> jenisHashMap;
    private DatabaseReference database;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private Uri imageUri;
    private static final int PICK_IMAGE = 1;
    private static final int PICK_Camera_IMAGE = 2;
    private Bitmap bitmap;
    private Lomba lomba;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dokumen_perlombaan);

        initialize();
    }

    public void initialize(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        database = FirebaseDatabase.getInstance().getReference();

        database = FirebaseDatabase.getInstance().getReference();
        CIVprofile = findViewById(R.id.CIV_photo);
        CIVbtnadd  = findViewById(R.id.CIV_btnadd);
        CIVbtnadd.setOnClickListener(this);
        etJudul = findViewById(R.id.ET_judul);
        etTanggal = findViewById(R.id.ET_tanggal);
        etTanggal.setOnClickListener(this);
        etAlamat = findViewById(R.id.ET_alamat);
        etDeskripsi = findViewById(R.id.ET_deskripsi);
        etJmlPeserta = findViewById(R.id.ET_jmlpeserta);
        etBiaya = findViewById(R.id.ET_biayapendaftaran);
        etTotalHadiah = findViewById(R.id.ET_totalhadiah);
        spnJenis = findViewById(R.id.spn_jenis);
        btnPengajuan = findViewById(R.id.btn_pengajuan);
        btnPengajuan.setOnClickListener(this);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        progressBar = findViewById(R.id.progressbar);
        lomba = new Lomba();
    }

    @Override
    public void onClick(View view) {
        if (view.equals(CIVbtnadd)){
            chooseImg();
        }
        if (view.equals(btnPengajuan)){
            submitPerlombaan();
        }
        if(view.equals(etTanggal)){
            Calendar newCalendar = Calendar.getInstance();

            /**
             * Initiate DatePicker dialog
             */
            datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    etTanggal.setText(dateFormatter.format(newDate.getTime()));
                }

            },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.loaddata();
    }

    public void loaddata(){

        progressBar.setVisibility(View.VISIBLE);

        database.child("jenis_lomba").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listJenis = new ArrayList<>();
                jenisHashMap = new HashMap<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Jenis mjenis = noteDataSnapshot.getValue(Jenis.class);
                    if (!mjenis.getKodejenis().equalsIgnoreCase("ALL")) {
                        mjenis.setKey(noteDataSnapshot.getKey());
                        listJenis.add(mjenis.getNamajenis());
                        jenisHashMap.put(mjenis.getNamajenis(),mjenis.getKodejenis());
                    }
                }
                spinneradapter = new ArrayAdapter<>(getApplicationContext(),R.layout.custom_spinner, listJenis);
                spnJenis.setAdapter(spinneradapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });
    }

    public void chooseImg(){
        final Dialog dialogPicture = new Dialog(this);
        dialogPicture.setCancelable(true);

        View view  = this.getLayoutInflater().inflate(R.layout.dialog_changepp, null);
        dialogPicture.setContentView(view);

        TextView photo = view.findViewById(R.id.tv_photo);
        TextView gallery =  view.findViewById(R.id.tv_gallery);


        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //define the file-name to save photo taken by Camera activity
                String fileName = "new-photo-name.jpg";
                //create parameters for Intent with filename
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, fileName);
                values.put(MediaStore.Images.Media.DESCRIPTION,"Image captured by camera");
                //imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
                imageUri = DokumenPerlombaanActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                //create new Intent
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, PICK_Camera_IMAGE);

                dialogPicture.dismiss();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_PICK);//
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    Log.e(e.getClass().getName(), e.getMessage(), e);
                }
                dialogPicture.dismiss();
            }
        });

        dialogPicture.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImageUri = null;
        String filePath = null;
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    selectedImageUri = data.getData();
                }
                break;
            case PICK_Camera_IMAGE:
                if (resultCode == RESULT_OK) {
                    //use imageUri here to access the image
                    selectedImageUri = imageUri;
		 		    	/*Bitmap mPic = (Bitmap) data.getExtras().get("data");
						selectedImageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), mPic, getResources().getString(R.string.app_name), Long.toString(System.currentTimeMillis())));*/
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(), "Picture was not taken", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Picture was not taken", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        if (selectedImageUri != null) {
            try {
                // OI FILE Manager
                String filemanagerstring = selectedImageUri.getPath();

                // MEDIA GALLERY
                String selectedImagePath = getPath(selectedImageUri);

                if (selectedImagePath != null) {
                    filePath = selectedImagePath;
                } else if (filemanagerstring != null) {
                    filePath = filemanagerstring;
                } else {
                    Toast.makeText(getApplicationContext(), "Unknown path",
                            Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                }

                if (filePath != null) {
                    decodeFile(filePath);
                } else {
                    bitmap = null;
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Internal error",
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }

    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public void decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 256;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);


        //Try to endcode bitmap
        if (bitmap != null){
            //try to setImage
            CIVprofile.setImageBitmap(bitmap);

            //Convert to base64
            String base64 = ImageUtil.convert(bitmap);
            lomba.setLogo_lomba(base64);
        }
    }

    public void getInputedData(){
        if (etJudul.getText() != null && !etJudul.getText().equals("")){
            lomba.setNama_lomba(etJudul.getText().toString());
        }else{
            etJudul.setError("Mohon Isi Judul Perlombaan");
        }
        if (etTanggal.getText() != null && !etTanggal.getText().equals("")){
            lomba.setTanggal_lomba(etTanggal.getText().toString());
        }else{
            etTanggal.setError("Mohon tentukan tanggal perlombaan");
        }
        if (etAlamat.getText() != null && !etAlamat.getText().equals("")){
            lomba.setAlamat_lomba(etAlamat.getText().toString());
        }else{
            etAlamat.setError("Mohon isi alamat perlombaan");
        }
        if (etDeskripsi.getText() != null && !etAlamat
                .getText().equals("")){
            lomba.setDeskripsi_lomba(etDeskripsi.getText().toString());
        }else{
            etDeskripsi.setError("Mohon tulis penjelasan perlombaan");
        }
        if (spnJenis.getSelectedItem() != null){
            String code  = jenisHashMap.get(spnJenis.getSelectedItem().toString());
            lomba.setJenis_lomba(code);
            lomba.setNama_jenis(spnJenis.getSelectedItem().toString());
        }
        if (etJmlPeserta.getText() != null){
            lomba.setJumlah_peserta(etJmlPeserta.getText().toString());
        }else{
            etJmlPeserta.setError("Mohon isi jumlah peserta");
        }
        if (etBiaya.getText() != null){
            try {
                Long biaya = Long.parseLong(etBiaya.getText().toString());
                lomba.setBiaya_pendaftaran(biaya);
            }catch (Exception e){
                etBiaya.setError("Mohon masukkan angka yang valid");
            }
        }else{
            etBiaya.setError("Mohon isi biaya pendaftaran");
        }

        if (etTotalHadiah.getText() != null){
            try {
                Long hadiah = Long.parseLong(etTotalHadiah.getText().toString());
                lomba.setTotal_hadiah(hadiah);
            }catch (Exception e){
                etTotalHadiah.setError("Mohon masukkan angka yang valid");
            }
        }else{
            etTotalHadiah.setError("Mohon isi total hadiah");
        }
        if (Constant.getLoginemail() != null){
            lomba.setPenyelenggara(Constant.getLoginemail());
        }

    }

    public void submitPerlombaan(){
        try {
            progressBar.setVisibility(View.VISIBLE);
            this.getInputedData();

            if (lomba.getLogo_lomba() != null) {

                String childKey = lomba.getJenis_lomba().concat(Constant.getLoginID()).concat(IDFactory.getTimeStamp());
                String hashKey = IDFactory.hashMD5(childKey);
                database.child("list_lomba").child(hashKey)
                        .setValue(this.lomba)
                        .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),
                                        "Perlombaan Berhasil diTambahkan",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
            }else{
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Mohon masukkan gambar perlombaan", Toast.LENGTH_SHORT).show();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
