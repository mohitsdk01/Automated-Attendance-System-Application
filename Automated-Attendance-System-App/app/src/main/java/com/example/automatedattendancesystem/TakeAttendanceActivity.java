package com.example.automatedattendancesystem;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.automatedattendancesystem.Constants.APIs;
import com.example.automatedattendancesystem.utils.APICaller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TakeAttendanceActivity extends AppCompatActivity {

    WifiManager mainWifiObj;
    WifiScanReceiver wifiReciever;
    String wifis[];
    Button fetchNamesBtn;
    ProgressBar progressBar;
    TextView countDownTimer;
    int counter = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        wifis = new String[0];
        countDownTimer = findViewById(R.id.timer);
        fetchNamesBtn = findViewById(R.id.fetch_names);


        progressBar = findViewById(R.id.progressBar);

        fetchNamesBtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        fetchNamesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTimerFinish(wifis);
            }
        });

        mainWifiObj = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiReciever = new WifiScanReceiver();
        mainWifiObj.startScan();
        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        new CountDownTimer(10000, 1000){
            public void onTick(long millisUntilFinished){
                countDownTimer.setText("Please Wait...\nAttendance is being taken\n"+String.valueOf(counter));
                counter--;
            }
            public  void onFinish(){
                unregisterReceiver(wifiReciever);

                onTimerFinish(wifis);
            }
        }.start();

    }

    private void onTimerFinish(String[] wifis) {

        fetchNamesBtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        countDownTimer.setText("Fetching Students Name...");

        String branch = getIntent().getStringExtra("Branch");
        String _class = getIntent().getStringExtra("Class");
        String subject = getIntent().getStringExtra("Subject");

        // RequestQueue to make API Request
        RequestQueue requestQueue = APICaller.getInstance(this).getRequestQueue();

        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("branch",branch);
        requestBody.put("class",_class);

        requestBody.put("studentIds",String.join(",",wifis));

//        For Testing
//        requestBody.put("studentIds",String.join(",",new String[]{"BEIT0001","BEIT0002","BEIT0003","BEIT0006","BEIT0007","BEIT0008","BEIT0009","BEIT0010","BECS0014","BEIT0006","BEIT0007","BEIT0003","BEIT0006","BEIT0007","BEIT0003"}));

        // Build API Request
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, APIs.GET_STUDENT_NAMES_FROM_STUDENT_ID_URL, new JSONObject(requestBody),
                response -> {

//                    Toast.makeText(TakeAttendanceActivity.this,response.toString(),Toast.LENGTH_SHORT).show();
                    Log.v("checking",response.toString());

                    if(response.has("studentNamesFromMac")){

                        try {
                            JSONArray studentNamesJson = response.getJSONArray("studentNamesFromMac");
                            String[] studentsList = new String[studentNamesJson.length()];
                            for(int i=0;i<studentNamesJson.length();i++){
                                studentsList[i] = studentNamesJson.get(i).toString();
                            }
                            Intent i = new Intent(TakeAttendanceActivity.this,ShowWifiListActivity.class);
                            i.putExtra("Branch",branch);
                            i.putExtra("Class",_class);
                            i.putExtra("Subject",subject);
                            i.putExtra("studentNames",studentsList);
                            i.putExtra("studentIds",wifis);
//                           i.putExtra("studentIds",new String[]{"BEIT0001","BEIT0002","BEIT0003","BEIT0006","BEIT0007","BEIT0008","BEIT0009","BEIT0010","BECS0014","BEIT0006","BEIT0007","BEIT0003","BEIT0006","BEIT0007","BEIT0003"});
                            startActivity(i);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else{
                        Toast.makeText(TakeAttendanceActivity.this,"Server Error!",Toast.LENGTH_SHORT).show();
                    }

                },
                error -> {
                    Log.v("checking",error.toString());
                    fetchNamesBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(TakeAttendanceActivity.this,"Server Error, Please Try again!"+error.toString(),Toast.LENGTH_SHORT).show();
                });

        // Execute API Request
        requestQueue.add(loginRequest);

    }

    class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
            wifis = new String[wifiScanList.size()];
            for(int i = 0; i < wifiScanList.size(); i++){
//                wifis[i] = wifiScanList.get(i).BSSID + " -> " + wifiScanList.get(i).SSID;
                    wifis[i] = wifiScanList.get(i).SSID;
//                Toast.makeText(TakeAttendanceActivity.this,wifiScanList.get(i).BSSID + " -> "+ wifiScanList.get(i).SSID,Toast.LENGTH_SHORT).show();
            }
            String filtered[] = new String[wifiScanList.size()];
            int counter = 0;
            for (String eachWifi : wifis) {
                String[] temp = eachWifi.split(",");

//                filtered[counter] = temp[0].substring(5).trim();//+"\n" + temp[2].substring(12).trim()+"\n" +temp[3].substring(6).trim();//0->SSID, 2->Key Management 3-> Strength
                filtered[counter] = eachWifi;
                counter++;
            }
        }
    }

}