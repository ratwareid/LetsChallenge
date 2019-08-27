package com.ratwareid.letschallenge;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

public class PermissionManager {

    static final Integer READ_STORAGE_PERMISSION_REQUEST_CODE=0x3;
    static final Integer CAMERA_PERMISSION_REQUEST_CODE=0x4;

    public boolean checkPermissionForReadExtertalStorage(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            int result = activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public boolean checkPermissionForCamera(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            int result = activity.checkSelfPermission(Manifest.permission.CAMERA);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForReadExtertalStorage(Activity activity){
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                READ_STORAGE_PERMISSION_REQUEST_CODE);
    }

    public void requestPermissionForCamera(Activity activity){
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST_CODE);
    }

}
