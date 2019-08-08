package com.ratwareid.letschallenge.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.ratwareid.letschallenge.R;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onBackPressed() {
        /*super.onBackPressed();*/
    }

    public void logout(View view) {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }
}
