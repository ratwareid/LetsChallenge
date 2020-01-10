package com.ratwareid.letschallenge.activity;

//***********************************//
// Created by Jerry Erlangga         //
// My Repo: www.github.com/ratwareid //
// Email : jerryerlangga82@gmail.com //
//***********************************//


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ratwareid.letschallenge.PermissionManager;
import com.ratwareid.letschallenge.R;
import com.ratwareid.letschallenge.fragment.AccountFragment;
import com.ratwareid.letschallenge.fragment.FavoriteFragment;
import com.ratwareid.letschallenge.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements BubbleNavigationChangeListener {
    FirebaseAuth firebaseAuth;
    BubbleNavigationLinearView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth = FirebaseAuth.getInstance();
        initialize();
    }

    @Override
    public void onBackPressed() {
        /*super.onBackPressed();*/
    }

    public void initialize(){
        PermissionManager.checkAllPermission(this);
    // kita set default nya Home Fragment
        loadFragment(new HomeFragment());
    // inisialisasi BottomNavigaionView
        bottomNavigationView = findViewById(R.id.bn_main);
    // beri listener pada saat item/menu bottomnavigation terpilih
        bottomNavigationView.setNavigationChangeListener(this);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void logout(View view) {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
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

    @Override
    public void onNavigationChanged(View view, int position) {
        switch (position){
            case 0:
                Fragment hfrag = new HomeFragment();
                loadFragment(hfrag);
                break;
            case 1:
                Fragment ffrag = new FavoriteFragment();
                loadFragment(ffrag);
                break;
            case 2:
                Fragment afrag = new AccountFragment();
                loadFragment(afrag);
                break;
        }

    }
}
