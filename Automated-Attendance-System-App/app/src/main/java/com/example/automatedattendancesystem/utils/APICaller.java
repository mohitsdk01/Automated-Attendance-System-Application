package com.example.automatedattendancesystem.utils;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class APICaller {
    private static APICaller mInstance;
    private RequestQueue mRequestQueue;

    private APICaller(Context context) {
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized APICaller getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new APICaller(context);
        }

        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
}