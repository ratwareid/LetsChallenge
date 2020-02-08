package com.ratwareid.letschallenge.activity;

//***********************************//
// Created by Jerry Erlangga         //
// My Repo: www.github.com/ratwareid //
// Email : jerryerlangga82@gmail.com //
//***********************************//


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ratwareid.letschallenge.R;
import com.roger.catloadinglibrary.CatLoadingView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textButtonRegister;
    private EditText ETemail, ETpassword, ETrepassword;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;

    CatLoadingView catLoadingView;

    private VideoView videoView;

    private TextView textSignIn;

    private static final String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialize();
    }

    private void initialize() {
        textButtonRegister = findViewById(R.id.textButtonRegister);
        textButtonRegister.setOnClickListener(this);
        ETemail = findViewById(R.id.inputEmail);
        ETpassword = findViewById(R.id.inputPassword);
        ETrepassword = findViewById(R.id.inputPasswordAgain);
        fAuth = FirebaseAuth.getInstance();
        catLoadingView = new CatLoadingView();
        videoView = findViewById(R.id.videoLogin);
        textSignIn = findViewById(R.id.textSignin);
        textSignIn.setOnClickListener(this);

        setVideoBackground();

        fStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User sedang login
                    moveToHome();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User sedang logout
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    private void setVideoBackground() {
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.background);
        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        videoView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.start();
    }

    private void moveToHome() {
        Intent myIntent = new Intent(RegisterActivity.this, HomeActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this);
        startActivity(myIntent, options.toBundle());
    }

    @Override
    public void onClick(View view) {
        if (view.equals(textButtonRegister)) {

            validateData();
        }else if(view.equals(textSignIn)){
            moveToLogin();
        }
    }

    private void moveToLogin() {
        finish();
    }

    private void validateData() {
        boolean failed = false;
        if (ETemail.getText().toString().equals("")) {
            ETemail.setError("Mohon Isikan Email Anda");
            failed = true;
        }
        if (ETpassword.getText().toString().equals("")) {
            ETpassword.setError("Mohon Isikan Password Anda");
            failed = true;
        }
        if (ETrepassword.getText().toString().equals("")) {
            ETrepassword.setError("Mohon Ketikkan Kembali Password Anda");
            failed = true;
        }

        if (!ETpassword.getText().toString().equalsIgnoreCase(ETrepassword.getText().toString())) {
            ETrepassword.setError("Password Yang Anda Ketikkan Tidak Sama");
            failed = true;
        }

        if (!failed) {
            catLoadingView.show(getSupportFragmentManager(), "Register");
            catLoadingView.setCanceledOnTouchOutside(false);
            signUp(ETemail.getText().toString(), ETpassword.getText().toString());
        }
    }

    private void signUp(final String email, String password) {

        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        /**
                         * Jika sign in gagal, tampilkan pesan ke user. Jika sign in sukses
                         * maka auth state listener akan dipanggil dan logic untuk menghandle
                         * signed in user bisa dihandle di listener.
                         */
                        if (!task.isSuccessful()) {
                            catLoadingView.dismiss();
                            task.getException().printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Proses Pendaftaran Gagal",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            catLoadingView.dismiss();
                            Toast.makeText(RegisterActivity.this, "Proses Pendaftaran Berhasil\n Email " + email,
                                    Toast.LENGTH_SHORT).show();
                            RegisterActivity.super.onBackPressed();
                        }

                        // rest of code
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fAuth.addAuthStateListener(fStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fStateListener != null) {
            fAuth.removeAuthStateListener(fStateListener);
        }
    }

}