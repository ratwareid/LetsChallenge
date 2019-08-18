package com.ratwareid.letschallenge;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

public class CRUDFirebaseHelper {

    public static void simpanData(DatabaseReference database, Object model, String childDB,
                                  final Activity activity, final ProgressDialog loading) {
        database.child(childDB)
                .push()
                .setValue(model)
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

    public static void ubahData(String key,DatabaseReference database, Object model, String childDB,
                                final Activity activity, final ProgressDialog loading) {
        database.child(childDB)
                .child(key)
                .setValue(model)
                .addOnSuccessListener(activity, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loading.dismiss();
                        Toast.makeText(activity.getApplicationContext(),
                                "Data Berhasil diedit",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
