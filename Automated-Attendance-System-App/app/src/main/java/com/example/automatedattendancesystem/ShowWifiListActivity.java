package com.example.automatedattendancesystem;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.automatedattendancesystem.Constants.APIs;
import com.example.automatedattendancesystem.utils.APICaller;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ShowWifiListActivity extends ListActivity {

    ListView list;
    File filePath;
    TextView noStudents;
    Button markAttendance;
    ProgressBar progressBar;

    TextView classNametv,branchNametv,subjectTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_wifi_list);

        noStudents = findViewById(R.id.zero_students);
        markAttendance = findViewById(R.id.mark_attendance_btn);

        classNametv = findViewById(R.id.class_name);
        progressBar = findViewById(R.id.progressBar);
        branchNametv = findViewById(R.id.branch_name);
        subjectTv = findViewById(R.id.sub_name);

        String branch = getIntent().getStringExtra("Branch");
        String _class = getIntent().getStringExtra("Class");
        String subject = getIntent().getStringExtra("Subject");

        branchNametv.setText("Branch : " + branch);
        classNametv.setText("Class : " + _class);
        subjectTv.setText("Subject : " + subject);


        list = getListView();
        String studentNames[] = getIntent().getStringArrayExtra("studentNames");
        String studentIds[] = getIntent().getStringArrayExtra("studentIds");

        if(studentNames.length==0){
            noStudents.setVisibility(View.VISIBLE);
            markAttendance.setEnabled(false);
        }else{
            markAttendance.setVisibility(View.VISIBLE);
            list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.list_item,R.id.label, studentNames));
        }

        markAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markStudentAttendance();
            }
        });

    }

    private void markStudentAttendance() {

        markAttendance.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        String branch = getIntent().getStringExtra("Branch");
        String _class = getIntent().getStringExtra("Class");
        String subject = getIntent().getStringExtra("Subject");
        String studentNames[] = getIntent().getStringArrayExtra("studentNames");
        String studentIds[] = getIntent().getStringArrayExtra("studentIds");

        // RequestQueue to make API Request
        RequestQueue requestQueue = APICaller.getInstance(this).getRequestQueue();

        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("branch",branch);
        requestBody.put("class",_class);
        requestBody.put("subject",subject);
        requestBody.put("studentIds",String.join(",",studentIds));

        Log.v("checking",requestBody.toString());

        // Build API Request
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, APIs.MARK_ATTENDANCE_URL, new JSONObject(requestBody),
                response -> {

                        Log.v("checking",response.toString());

                    if(response.has("message")){
                        try {
                            if(response.get("message").toString().equals("Attendance Marked!")){
                                Toast.makeText(ShowWifiListActivity.this,"Attendance Marked Successfully!!",Toast.LENGTH_SHORT).show();
                                onAttendanceMarked();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(ShowWifiListActivity.this,"Server Error!" + response.toString(),Toast.LENGTH_SHORT).show();
                    }

                },
                error -> {
                    markAttendance.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ShowWifiListActivity.this,"Server Error, Please Try again!"+error.toString(),Toast.LENGTH_SHORT).show();
                });

        // Execute API Request
        requestQueue.add(loginRequest);

    }

    private void onAttendanceMarked() {
        saveAttendanceReport();
        Intent i = new Intent(ShowWifiListActivity.this,AccountChooserActivity.class);
        startActivity(i);
        finish();
    }

    public void saveAttendanceReport(){

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH_mm_ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        String branch = getIntent().getStringExtra("Branch");
        String _class = getIntent().getStringExtra("Class");
        String subject = getIntent().getStringExtra("Subject");

        if(branch.equals("Information Technology")){
            branch = "IT";
        }else if(branch.equals("Computer Science")){
            branch = "CS";
        }

        String appFolderName  = "Automated Attendance System";

        File appDirectory;

        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){
            appDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+File.separator+appFolderName);
        } else{
            appDirectory = new File(Environment.getExternalStorageDirectory(),Environment.DIRECTORY_DOWNLOADS+"/"+appFolderName);
        }

       if(!appDirectory.exists() && !appDirectory.isDirectory())
        {
            if (appDirectory.mkdirs())
            {
                Log.i("CreateDir","App dir created");
            }
            else
            {
                Log.w("CreateDir","Unable to create app dir!");
            }
        }
        else
        {
            Log.i("CreateDir","App dir already exists");
        }


        SimpleDateFormat currDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currDayFolder = currDate.format(new Date());

        File currDayDir;

        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){
            currDayDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+File.separator+appFolderName+File.separator+currDayFolder);

        } else{
            currDayDir = new File(Environment.getExternalStorageDirectory(),Environment.DIRECTORY_DOWNLOADS+"/"+appFolderName+File.separator+currDayFolder);
        }

        if(!currDayDir.exists() && !currDayDir.isDirectory())
        {
            if (currDayDir.mkdirs())
            {
                Log.i("CreateDir","currDayDir dir created");
            }
            else
            {
                Log.w("CreateDir","Unable to create app dir!");
            }
        }
        else
        {
            Log.i("CreateDir","currDayDir dir already exists");
        }



        String fileName = _class+ "_" + branch +"_"+ subject +" " + currentDateandTime + ".xls";


        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.Q){
            filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/"+appFolderName+"/"+currDayFolder+"/"+fileName);
        } else{
            String tempPath = Environment.getExternalStorageDirectory() +"/"+ Environment.DIRECTORY_DOWNLOADS + "/"+appFolderName+"/"+currDayFolder+"/"+fileName;
            filePath = new File(tempPath);
        }

        String studentNames[] = getIntent().getStringArrayExtra("studentNames");

        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Attendance Sheet ");

        HSSFRow hssfRow1 = hssfSheet.createRow(0);

        HSSFCell rollNCell = hssfRow1.createCell(0);
        rollNCell.setCellValue("Roll No.");

        HSSFCell cellName1 = hssfRow1.createCell(1);
        cellName1.setCellValue("Name");

        for(int i=0;i<studentNames.length;i++){
            HSSFRow hssfRow = hssfSheet.createRow(i+1);

            HSSFCell cellRollNo = hssfRow.createCell(0);
            cellRollNo.setCellValue(studentNames[i].split(" : ")[0]+"");

            HSSFCell cellName = hssfRow.createCell(1);
            cellName.setCellValue(studentNames[i].split(" : ")[1]+"");

        }
        hssfSheet.setColumnWidth(1,20*230);


//        hssfSheet.setColumnWidth(2, 10);
        try {
            if (!filePath.exists()){
                filePath.createNewFile();
            }


            FileOutputStream fileOutputStream= new FileOutputStream(filePath);
            hssfWorkbook.write(fileOutputStream);
            Toast.makeText(this,"Sheet Downloaded",Toast.LENGTH_SHORT).show();

            if (fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}