package com.example.automatedattendancesystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TeacherPrivateKey extends AppCompatActivity {

    static String key = "12345";
    AppCompatButton validate;
    EditText secretKeyEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_private_key);

        validate = findViewById(R.id.validateBtn);
        secretKeyEdit = findViewById(R.id.Secretkey);

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(secretKeyEdit.getText().toString().equals("12345")){
                    Intent validation = new Intent(TeacherPrivateKey.this,TeacherHomeActivity.class);
                    startActivity(validation);
                    finish();
                }else{
                    Toast.makeText(TeacherPrivateKey.this,"Enter Valid Key",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}