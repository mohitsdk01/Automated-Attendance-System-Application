package com.example.automatedattendancesystem.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "local_storage";
    private static final String STUDENT_ID = "StudentID";
    private static final String LOGIN_STATE = "login_state";

    private SharedPrefManager(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }

        return mInstance;
    }

    public void studentLogin(String studentId){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOGIN_STATE,true);
        editor.putString(STUDENT_ID,studentId);
        editor.apply();
    }

    public boolean isStudentLoggedIn(){
        return sharedPreferences.getBoolean(LOGIN_STATE,false);
    }

}