package com.example.camerairis_v2;
import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class LaunchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        Log.d(TAG,"iris");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //check permission
            if(!hasPermissions(PERMISSIONS)){
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }else{
                Intent mainIntent = new Intent(LaunchActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }
    }

    //--permission--//
    static final int PERMISSIONS_REQUEST_CODE =1000;
    String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private boolean hasPermissions(String[] permissions){
        Log.d(TAG,"iris");
        int result;

        //check permissions on 'PERMISSIONS[]'
        for(String perms : permissions){
            result = ContextCompat.checkSelfPermission(this,perms);

            if(result== PackageManager.PERMISSION_DENIED){
                return false;
            }
        }
        //all permissions allowed
        return true;
    }
    public void onRequestPermisisonsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        switch (requestCode){
            case PERMISSIONS_REQUEST_CODE:
                if(grantResults.length > 0){
                    boolean cameraPermissionAccepted = grantResults[0]
                            == PackageManager.PERMISSION_GRANTED;
                    boolean diskPermissionAccepted = grantResults[1]
                            == PackageManager.PERMISSION_GRANTED;

                    if(!cameraPermissionAccepted || !diskPermissionAccepted)
                        showDialogForPermission("using CameraApp need permission.");
                    else {
                        Intent mainIntent = new Intent(LaunchActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }
                }
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String s) {
        Log.d(TAG,"iris");
        AlertDialog.Builder builder = new AlertDialog.Builder(LaunchActivity.this);
        builder.setTitle("Alert");
        builder.setMessage(s);
        builder.setCancelable(false);
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    finish();
            }
        });
        builder.create().show();
    }
}
