package com.ratwareid.letschallenge.activity;

//***********************************//
// Created by Jerry Erlangga         //
// My Repo: www.github.com/ratwareid //
// Email : jerryerlangga82@gmail.com //
//***********************************//


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ratwareid.letschallenge.PermissionManager;
import com.ratwareid.letschallenge.R;
import com.roger.catloadinglibrary.CatLoadingView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textButtonLogin;
    private TextView textSignup;
    private Context context;
    private EditText ETemail,ETpassword;
    private FirebaseAuth fAuth;
    private VideoView videoView;
    private CatLoadingView catLoadingView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAnimation();
        setContentView(R.layout.activity_login);
        initialize();
    }

    private void initialize(){

        PermissionManager.checkAllPermission(this);

        textButtonLogin = findViewById(R.id.textButtonLogin);
        textButtonLogin.setOnClickListener(this);
        textSignup = findViewById(R.id.textSignup);
        textSignup.setOnClickListener(this);
        ETpassword = findViewById(R.id.MAT_editpassword);
        ETemail = findViewById(R.id.MAT_editemail);
        context = this.getApplicationContext();
        fAuth = FirebaseAuth.getInstance();
        videoView = findViewById(R.id.videoLogin);
        catLoadingView = new CatLoadingView();

        setBackgroundVideo();

        if(fAuth.getCurrentUser() != null){
            moveToHome();
        }
    }

    private void setBackgroundVideo() {
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
    public void onClick(View view) {
        if (view.equals(textButtonLogin)){

            if (ETemail.getText().toString().equals("") || ETpassword.getText().toString().equals("")){
                Toast.makeText(context, "Harap Masukkan Username dan Password", Toast.LENGTH_SHORT).show();
            }else{
                try {
                    doSignIn(ETemail.getText().toString(),ETpassword.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        if (view.equals(textSignup)){
            Intent myIntent = new Intent(this, RegisterActivity.class);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(myIntent,options.toBundle());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.start();
    }

    public void setAnimation() {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.START);
        slide.setInterpolator(new DecelerateInterpolator());
        getWindow().setExitTransition(slide);
        getWindow().setEnterTransition(slide);
    }

    private void doSignIn(String email,String password) throws Exception{
        catLoadingView.setCanceledOnTouchOutside(false);
        catLoadingView.show(getSupportFragmentManager(), "Login");
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    moveToHome();
                    catLoadingView.dismiss();
                }
                else {
                    catLoadingView.dismiss();
                    Toast.makeText(LoginActivity.this,
                            "Username atau password yang anda masukkan salah", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void moveToHome(){
        finish();
        Intent myIntent = new Intent(LoginActivity.this, HomeActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this);
        startActivity(myIntent,options.toBundle());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
            return;
        }
    }
}
