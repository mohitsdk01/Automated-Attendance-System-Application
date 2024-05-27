package com.example.automatedattendancesystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StudentHomeActivity extends AppCompatActivity {

   Button logoutBtn;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "local_storage";
    private static final String STUDENT_ID = "StudentID";
    private static final String LOGIN_STATE = "login_state";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        logoutBtn = findViewById(R.id.logout_btn);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(LOGIN_STATE,false);
                editor.apply();
                Intent intent = new Intent(StudentHomeActivity.this, AccountChooserActivity.class);
                startActivity(intent);
                finish();
            }
        });


//        ActionBar actionBar = getSupportActionBar();
//        assert actionBar != null;
//        actionBar.show();

    }
}