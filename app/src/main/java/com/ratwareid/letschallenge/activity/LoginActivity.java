package com.ratwareid.letschallenge.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ratwareid.letschallenge.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialButton btnLogin;
    private MaterialButton btnRegis;
    private Context context;
    private TextInputEditText ETpassword,ETemail;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAnimation();
        setContentView(R.layout.activity_login);
        initialize();
    }

    private void initialize(){
        btnLogin = findViewById(R.id.MAT_btnlogin);
        btnLogin.setOnClickListener(this);
        btnRegis = findViewById(R.id.MAT_btnregis);
        btnRegis.setOnClickListener(this);
        ETpassword = findViewById(R.id.MAT_editpassword);
        ETemail = findViewById(R.id.MAT_editemail);
        context = this.getApplicationContext();
        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null){
            moveToHome();
        }
    }


    @Override
    public void onClick(View view) {
        if (view.equals(btnLogin)){

            try {
                doSignIn(ETemail.getText().toString(),ETpassword.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (view.equals(btnRegis)){
            Intent myIntent = new Intent(this, RegisterActivity.class);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(myIntent,options.toBundle());
        }
    }

    public void setAnimation() {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.START);
        slide.setInterpolator(new DecelerateInterpolator());
        getWindow().setExitTransition(slide);
        getWindow().setEnterTransition(slide);
    }

    private void doSignIn(String email,String password) throws Exception{
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    moveToHome();
                }
                else {
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
}
