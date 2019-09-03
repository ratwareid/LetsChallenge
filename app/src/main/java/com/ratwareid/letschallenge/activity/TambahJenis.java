package com.ratwareid.letschallenge.activity;

//***********************************//
// Created by Jerry Erlangga         //
// My Repo: www.github.com/ratwareid //
// Email : jerryerlangga82@gmail.com //
//***********************************//


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ratwareid.letschallenge.ImageUtil;
import com.ratwareid.letschallenge.R;
import com.ratwareid.letschallenge.model.Jenis;

import de.hdodenhof.circleimageview.CircleImageView;

public class TambahJenis extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "tamvan";
    private DatabaseReference database;

    private EditText etNama, etKode;
    private ProgressBar progressBar;
    private Button btn_cancel, btn_save;
    private CircleImageView CIVchange,CIVphoto;
    private Uri imageUri;
    private static final int PICK_IMAGE = 1;
    private static final int PICK_Camera_IMAGE = 2;
    private Bitmap bitmap;

    private String stID, stNama, stKode,stImg;

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
        btn_save.setOnClickListener(this);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);

        CIVchange = findViewById(R.id.CIV_btnadd);
        CIVchange.setOnClickListener(this);
        CIVphoto = findViewById(R.id.CIV_jenis);
        etNama.setText(stNama);
        etKode.setText(stKode);
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

    @Override
    public void onClick(View view) {
        if (view.equals(btn_save)){

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
                    submitUser(new Jenis(Snama, Skode.toUpperCase(), stImg));
                }
            }
        }

        if (view.equals(btn_cancel)){
            finish();
        }

        if (view.equals(CIVchange)){
            chooseImg();
        }
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
                imageUri = TambahJenis.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
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
            CIVphoto.setImageBitmap(bitmap);

            //Convert to base64
            stImg = ImageUtil.convert(bitmap);

        }
    }
}
