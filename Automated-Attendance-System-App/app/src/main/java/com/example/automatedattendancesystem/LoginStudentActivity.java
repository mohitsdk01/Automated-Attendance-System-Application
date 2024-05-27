package com.example.automatedattendancesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.automatedattendancesystem.Constants.APIs;
import com.example.automatedattendancesystem.utils.APICaller;
import com.example.automatedattendancesystem.utils.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginStudentActivity extends AppCompatActivity {

    EditText studentIdET, passwordET;
    Button loginBtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_student);

        initializeView();

        if(SharedPrefManager.getInstance(this).isStudentLoggedIn()){
            Intent intent = new Intent(LoginStudentActivity.this, StudentHomeActivity.class);
            startActivity(intent);
            finish();
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String studentId = studentIdET.getText().toString();
                String password = passwordET.getText().toString();

                if(studentId.isEmpty()){
                    studentIdET.setError("Email Required!!");
                    return;
                }

                if(password.isEmpty()){
                    passwordET.setError("Password Required!!");
                    return;
                }

                doLogin(studentId,password);
            }
        });

    }

    private void initializeView() {
        studentIdET = findViewById(R.id.student_id_edit_text);
        passwordET = findViewById(R.id.password_edit_text);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progressBar);
    }

    private void doLogin(String studentId, String password) {
        loginBtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        // RequestQueue to make API Request
        RequestQueue requestQueue = APICaller.getInstance(this).getRequestQueue();

        // Data To Send As A Body Or Parameters To The Login API
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("studentId", studentId);
        requestBody.put("password", password);

        // Build API Request
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, APIs.SIGN_IN_URL, new JSONObject(requestBody),
                response -> {

                    loginBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    if(response.has("message")){
                        try {
                            if(response.get("message").toString().equals("student exist")){
                                Toast.makeText(LoginStudentActivity.this,"Login Success.",Toast.LENGTH_SHORT).show();
                                onLoginSuccess(studentId);
                            }else{
                                Toast.makeText(LoginStudentActivity.this,"Enter Valid Details",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(LoginStudentActivity.this,"Try Again!",Toast.LENGTH_SHORT).show();
                    }

                },
                error -> {

                    loginBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(LoginStudentActivity.this,"Error Occurred, Try again!",Toast.LENGTH_SHORT).show();
                });

        // Execute API Request
        requestQueue.add(loginRequest);

    }

    private void onLoginSuccess(String studentId) {
        SharedPrefManager.getInstance(this).studentLogin(studentId);
        Intent intent = new Intent(LoginStudentActivity.this, StudentHomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void student_registration_redirect(View view) {
        Intent registrationStudent = new Intent(this, StudentRegistrationActivity.class);
        startActivity(registrationStudent);
    }
}