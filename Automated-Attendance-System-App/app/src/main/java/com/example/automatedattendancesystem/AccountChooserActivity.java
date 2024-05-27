package com.example.automatedattendancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Method;

public class AccountChooserActivity extends AppCompatActivity {

    Button teacher_chooser;
    Button student_chooser;
    Button change_ssid;

    private static final int LOCATION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_chooser);

        teacher_chooser = findViewById(R.id.account_chooser_teacher_btn);
        student_chooser = findViewById(R.id.account_chooser_student_btn);
        change_ssid = findViewById(R.id.change_ssid);

        if(!hasLocationPermission()){
            requestLocationPermission();
        }

    }

    private boolean hasLocationPermission(){
        // Check Camera Permission Granted
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ;
    }

    private void requestLocationPermission(){
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE}, LOCATION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == LOCATION_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Permission", "Camera Permission Granted");
            }else{
                Log.d("Permission", "Camera Permission Denied");
                requestLocationPermission();
            }
        }
    }

    public void teacher_chooser(View view) {
        Intent teacher_btn = new Intent(this, TeacherPrivateKey.class);
        startActivity(teacher_btn);
    }

    public void student_chooser(View view) {
        Intent student_btn = new Intent(this, LoginStudentActivity.class);
        startActivity(student_btn);
    }

    public void changeSSID(View view) {
            try {
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                Method getConfigMethod = wifiManager.getClass().getMethod("getWifiApConfiguration");
                WifiConfiguration wifiConfig = (WifiConfiguration) getConfigMethod.invoke(wifiManager);

                wifiConfig.SSID = "BEIT0001";


                Method setConfigMethod = wifiManager.getClass().getMethod("setWifiApConfiguration", WifiConfiguration.class);
                setConfigMethod.invoke(wifiManager, wifiConfig);

            }
            catch (Exception e) {
                e.printStackTrace();
            }
    }
}
