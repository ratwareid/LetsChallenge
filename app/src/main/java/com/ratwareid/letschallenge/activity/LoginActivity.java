package com.ratwareid.letschallenge.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.google.android.material.button.MaterialButton;
import com.ratwareid.letschallenge.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialButton btnLogin;
    private Context context;

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
        context = this.getApplicationContext();
    }


    @Override
    public void onClick(View view) {
        if (view.equals(btnLogin)){
            Intent myIntent = new Intent(this, HomeActivity.class);
            // Check if we're running on Android 5.0 or higher
            if(Build.VERSION.SDK_INT>20){
                ActivityOptions options =
                        ActivityOptions.makeSceneTransitionAnimation(this);
                startActivity(myIntent,options.toBundle());
            }else {
                startActivity(myIntent);
            }
        }
    }

    public void setAnimation() {
        if (Build.VERSION.SDK_INT > 20) {
            Slide slide = new Slide();
            slide.setSlideEdge(Gravity.LEFT);
            slide.setDuration(400);
            slide.setInterpolator(new DecelerateInterpolator());
            getWindow().setExitTransition(slide);
            getWindow().setEnterTransition(slide);
        }
    }

}
