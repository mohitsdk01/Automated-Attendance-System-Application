package com.example.automatedattendancesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.automatedattendancesystem.utils.SharedPrefManager;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {
    private static final int SPLASH_SCREEN_TIME_OUT= 2000;

    ImageView splashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        splashImage = findViewById(R.id.spash_image_view);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_animation);
        splashImage.startAnimation(animation);

        splashImage.startAnimation(animation); // start animation



        new Handler().postDelayed(() -> {
            if(SharedPrefManager.getInstance(this).isStudentLoggedIn()){
                Intent intent = new Intent(SplashScreen.this, StudentHomeActivity.class);
                startActivity(intent);
                finish();
            }else {
                Intent i = new Intent(SplashScreen.this,
                        AccountChooserActivity.class);
                //Intent is used to switch from one activity to another.

                startActivity(i);
                //invoke the SecondActivity.

                finish();
                //the current activity will get finished.
            }
        }, SPLASH_SCREEN_TIME_OUT);


    }
}