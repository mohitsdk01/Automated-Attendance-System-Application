package com.example.automatedattendancesystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TeacherHomeActivity extends AppCompatActivity {

    AppCompatButton takeAttendanceBtn,seeAttendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);

        takeAttendanceBtn = findViewById(R.id.take_attendance_btn);
        seeAttendance = findViewById(R.id.see_attendance_btn);

        takeAttendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent validation = new Intent(TeacherHomeActivity.this,SubjectSelectionActivity.class);
                startActivity(validation);
//                finish();
            }
        });

        seeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent validation = new Intent(TeacherHomeActivity.this,GetAttendanceActivity.class);
                startActivity(validation);
            }
        });

    }
}