package com.ratwareid.letschallenge.fragment;

//***********************************//
// Created by Jerry Erlangga         //
// My Repo: www.github.com/ratwareid //
// Email : jerryerlangga82@gmail.com //
//***********************************//


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratwareid.letschallenge.Constant;
import com.ratwareid.letschallenge.ImageUtil;
import com.ratwareid.letschallenge.PermissionManager;
import com.ratwareid.letschallenge.R;
import com.ratwareid.letschallenge.activity.EditProfileActivity;
import com.ratwareid.letschallenge.activity.HomeActivity;
import com.ratwareid.letschallenge.activity.ListLombaActivity;
import com.ratwareid.letschallenge.activity.ListLombaDetailActivity;
import com.ratwareid.letschallenge.activity.ListLombaDibuatActivity;
import com.ratwareid.letschallenge.activity.ListLombaTerdaftarActivity;
import com.ratwareid.letschallenge.activity.LoginActivity;
import com.ratwareid.letschallenge.activity.TambahJenis;
import com.ratwareid.letschallenge.adapter.JenisAdapter;
import com.ratwareid.letschallenge.model.Jenis;
import com.ratwareid.letschallenge.model.Lomba;
import com.ratwareid.letschallenge.model.Userdata;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class AccountFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private MaterialButton btnTJ;
    private Button btnLogout,btnEditProfile;
    private TextView etEmail;
    private TextView tvBio,tvNoTlp,tvNama,tvAlamat;
    private FirebaseAuth fAuth;
    private CircleImageView CIVchangeimage,CIVprofile;
    private Uri imageUri;
    private static final int PICK_IMAGE = 1;
    private static final int PICK_Camera_IMAGE = 2;
    private Bitmap bitmap;
    private ProgressBar progressBar,progressBarPhoto;
    private DatabaseReference database;
    private HomeActivity activity;
    private Userdata userdata;
    private MaterialCardView cardred,cardblue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initialize(view);
        return view;
    }

    private void initialize(View view) {

        database = FirebaseDatabase.getInstance().getReference();
        activity = (HomeActivity) this.getActivity();
        progressBar = view.findViewById(R.id.progressbar);
        progressBarPhoto = view.findViewById(R.id.progressbarphoto);

        btnTJ = view.findViewById(R.id.btn_TJ);
        btnTJ.setOnClickListener(this);
        btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnEditProfile.setOnClickListener(this);

        if (Constant.getLoginemail().equalsIgnoreCase(Constant.rootEmail)){
            btnTJ.setVisibility(View.VISIBLE);
            btnTJ.setEnabled(true);
        }
        etEmail = view.findViewById(R.id.et_email);
        tvBio = view.findViewById(R.id.tv_bio);

        etEmail.setText(Constant.getLoginemail());
        fAuth = FirebaseAuth.getInstance();
        CIVchangeimage = view.findViewById(R.id.CIV_btnadd);
        CIVchangeimage.setOnClickListener(this);

        tvNama = view.findViewById(R.id.tv_nama);
        tvBio = view.findViewById(R.id.tv_bio);
        tvNoTlp = view.findViewById(R.id.tv_notlp);
        tvAlamat = view.findViewById(R.id.tv_alamat);
        CIVprofile = view.findViewById(R.id.CIV_profile);

        cardred = view.findViewById(R.id.card_red);
        cardred.setOnClickListener(this);
        cardblue = view.findViewById(R.id.card_blue);
        cardblue.setOnClickListener(this);

        tvBio.setMovementMethod(new ScrollingMovementMethod());
        tvBio.setOnTouchListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnTJ)){
            Intent intent = new Intent(getActivity(), TambahJenis.class);
            startActivity(intent);
        }

        if (view.equals(btnLogout)){
            try {
                fAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (view.equals(CIVchangeimage)){
            showDialog();
        }

        if (view.equals(btnEditProfile)){
            ubahProfile();
        }
        if (view.equals(cardred)){
            lihatLombaTerdaftar();
        }
        if (view.equals(cardblue)){
            lihatLombaDibuat();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        loaddata();
    }

    public void loaddata(){
        progressBar.setVisibility(View.VISIBLE);

        database.child("userdata").child(Constant.getLoginID()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userdata = dataSnapshot.getValue(Userdata.class);
                placedata(userdata);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void placedata(Userdata userdata){
        if (userdata != null){
            if (userdata.getPhoto() != null) {
                String imgdecoded = userdata.getPhoto();
                byte[] decodedString = Base64.decode(imgdecoded, Base64.DEFAULT);
                CIVprofile.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
            }
            if (userdata.getNama() != null) {
                tvNama.setText(userdata.getNama());
            }
            if (userdata.getBio() != null){
                tvBio.setText(userdata.getBio());
            }
            if (userdata.getNo_tlp() != null) {
                tvNoTlp.setText(userdata.getNo_tlp());
            }
            if (userdata.getAlamat() != null) {
                tvAlamat.setText(userdata.getAlamat());
            }
        }
    }

    public void ubahProfile(){
        Intent myIntent = new Intent(activity, EditProfileActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity);
        activity.startActivity(myIntent,options.toBundle());
    }

    public void showDialog(){
        final Dialog dialogPicture = new Dialog(getActivity());
        dialogPicture.setCancelable(true);

        View view  = getActivity().getLayoutInflater().inflate(R.layout.dialog_changepp, null);
        dialogPicture.setContentView(view);

        TextView photo = (TextView) view.findViewById(R.id.tv_photo);
        TextView gallery = (TextView) view.findViewById(R.id.tv_gallery);


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
                imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
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
                    Toast.makeText(getActivity().getApplicationContext(),
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

        progressBarPhoto.setVisibility(View.VISIBLE);

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
                    Toast.makeText(getContext(), "Picture was not taken", Toast.LENGTH_SHORT).show();
                    progressBarPhoto.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getContext(), "Picture was not taken", Toast.LENGTH_SHORT).show();
                    progressBarPhoto.setVisibility(View.GONE);
                }
                break;
        }

        if(selectedImageUri != null){
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
                    Toast.makeText(getActivity().getApplicationContext(), "Unknown path",
                            Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                    progressBarPhoto.setVisibility(View.GONE);
                }

                if (filePath != null) {
                    decodeFile(filePath);
                } else {
                    bitmap = null;
                }
            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "Internal error",
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
                progressBarPhoto.setVisibility(View.GONE);
            }
        }

    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
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

    @SuppressLint("ShowToast")
    public void decodeFile(String filePath) throws Exception {
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
        boolean failed = false;

        //Try to endcode bitmap
        if (bitmap != null){
            bitmap = ImageUtil.repositionImage(filePath,bitmap);
            String base64 = ImageUtil.convert(bitmap);
            uploadphoto(base64);
        }
    }


    public void uploadphoto(String base64){

        database.child("userdata").child(Constant.getLoginID()).child("photo")
                .setValue(base64)
                .addOnSuccessListener(activity, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(activity.getApplicationContext(),
                                "Berhasil Mengunggah Photo",
                                Toast.LENGTH_SHORT).show();
                        progressBarPhoto.setVisibility(View.GONE);
                    }
                });
    }

    public void lihatLombaTerdaftar(){
        Intent myIntent = new Intent(activity, ListLombaTerdaftarActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity);
        activity.startActivity(myIntent,options.toBundle());
    }

    public void lihatLombaDibuat(){
        Intent myIntent = new Intent(activity, ListLombaDibuatActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity);
        activity.startActivity(myIntent,options.toBundle());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.getParent().requestDisallowInterceptTouchEvent(true);
        return false;
    }
}