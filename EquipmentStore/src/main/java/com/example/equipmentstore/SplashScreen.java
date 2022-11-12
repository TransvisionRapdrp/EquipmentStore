package com.example.equipmentstore;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.equipmentstore.extra.FunctionCall;
import com.example.equipmentstore.extra.SharedPreferanceStore;
import com.example.equipmentstore.login.LoginActivity;
import com.example.equipmentstore.mpin_login.LoginReg;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.equipmentstore.extra.constants.MyPREFERENCES;

public class SplashScreen extends AppCompatActivity {
    public static final int RequestPermissionCode = 1;
    FunctionCall functionCall;
    SharedPreferences preferences;
    Intent in;
    LinearLayout linearLayout;
    public static String version="";
    SharedPreferanceStore store;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        functionCall = new FunctionCall();
        preferences = getSharedPreferences(MyPREFERENCES, getApplication().MODE_PRIVATE);
        store = SharedPreferanceStore.getInstance(getApplicationContext());
        linearLayout = findViewById(R.id.linearLayout2);
        if (!functionCall.compareDates(store.getString(SharedPreferanceStore.Key.REMARK_DATE,
                functionCall.dateSet()))){
            store.put(SharedPreferanceStore.Key.REMARK_FLG,"0");
        }

        getAppVersion();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPermissionsMandAbove();
            }
        }, 1000);
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------
    @TargetApi(23)
    public void checkPermissionsMandAbove() {
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= 23) {
            if (checkPermission()) {
                movetoLogin();
            } else {
                requestPermission();
            }
        } else {
            movetoLogin();
        }
    }

    //=------------------------------------------------------------------------------------------------------------------------------------------
    private void movetoLogin() {

        boolean hasLoggedIn = preferences.getBoolean("hasLoggedIn", false);

        if (hasLoggedIn) {
            in = new Intent(SplashScreen.this, LoginReg.class);
        } else{
            in = new Intent(SplashScreen.this, LoginActivity.class);
        }
        startActivity(in);
        finish();

//        if (functionCall.isInternetOn(this)) {
//            Intent in = new Intent(SplashScreen.this, LoginActivity.class);
//            startActivity(in);
//            finish();
//        } else {
//            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
//            startActivity(intent);
//        }
    }

    //----------------------------------------------------------------------------------------------------------------------------------------
    private void requestPermission() {//6
        ActivityCompat.requestPermissions(SplashScreen.this, new String[]{
                READ_PHONE_STATE,
                WRITE_EXTERNAL_STORAGE,
                ACCESS_FINE_LOCATION,
                CAMERA
        }, RequestPermissionCode);
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------
    private boolean checkPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int FourthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FourthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    //---------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RequestPermissionCode) {
            if (grantResults.length > 0) {
                boolean ReadPhoneStatePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean ReadStoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean ReadLocationPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                boolean ReadLogsPermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                if (ReadPhoneStatePermission && ReadStoragePermission && ReadLocationPermission && ReadLogsPermission) {
                    movetoLogin();
                } else {
                    Toast.makeText(SplashScreen.this, "Required All Permissions to granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    private void getAppVersion() {
        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
