package com.example.automatedattendancesystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.automatedattendancesystem.Constants.APIs;
import com.example.automatedattendancesystem.models.StudentAttendanceModel;
import com.example.automatedattendancesystem.models.StudentsModel;
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
import java.util.Locale;
import java.util.Map;

public class GetAttendanceActivity extends AppCompatActivity {

    File filePath;
    AutoCompleteTextView branchDropDown;
    AutoCompleteTextView classDropDown;
    AutoCompleteTextView subjectDropDown;
    AppCompatButton seeAttendance;
    Button downloadBtn;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_attendance);

        downloadBtn = findViewById(R.id.download);
        branchDropDown = findViewById(R.id.dropdown_branch);
        classDropDown = findViewById(R.id.dropdown_class);
        subjectDropDown = findViewById(R.id.dropdown_subject);
        seeAttendance = findViewById(R.id.see_attendance_btn);
        progressBar = findViewById(R.id.progressBar);


        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAttendanceReport();
            }
        });

        seeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String branch = branchDropDown.getText().toString();
                String _class = classDropDown.getText().toString();
                String subject = subjectDropDown.getText().toString();

                if(branch.equals("Select Branch")){
                    branchDropDown.setError("Select Branch");
                    return;
                }
                else if(_class.equals("Select Class")){
                    classDropDown.setError("Select Class");
                    return;
                }
//                else if(subject.equals("Select Subject")){
//                    subjectDropDown.setError("Select Subject");
//                    return;
//                }
                else{

                    if(branch.equals("Information Technology")){
                        branch = "IT";
                    }else if(branch.equals("Computer Science")){
                        branch = "CS";
                    }

                    fetchAttendanceData(branch,_class);
                }

            }
        });

    }

    private void fetchAttendanceData(String branch, String _class) {

        seeAttendance.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        // RequestQueue to make API Request
        RequestQueue requestQueue = APICaller.getInstance(this).getRequestQueue();

        // Build API Request
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.GET, APIs.GET_ALL_STUDENT_DATA, null,
                response -> {

                    if(response.has("students")){
                         onDataReceived(response);
                    }else{
                        seeAttendance.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(GetAttendanceActivity.this,"Invalid Data Received",Toast.LENGTH_SHORT).show();
                    }

                },
                error -> {

//                    loginBtn.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);
                    seeAttendance.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(GetAttendanceActivity.this,"Error Occurred, Try again!",Toast.LENGTH_SHORT).show();
                });

        // Execute API Request
        requestQueue.add(loginRequest);



    }

    private void onDataReceived(JSONObject response) {

        StudentsModel[] studentsData = getStudentsData(response);

        String branch = branchDropDown.getText().toString();
        String _class = classDropDown.getText().toString();

        if (branch.equals("Information Technology")) {
            branch = "IT";
        } else if (branch.equals("Computer Science")) {
            branch = "CS";
        }
        // RequestQueue to make API Request
        RequestQueue requestQueue = APICaller.getInstance(this).getRequestQueue();

        // Data To Send As A Body Or Parameters To The Login API
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("branch", branch);
        requestBody.put("class", _class);

        // Build API Request
        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, APIs.GET_ATTENDANCE_URL, new JSONObject(requestBody),
                response1 -> {

                    if(response1.has("students")){
                        Toast.makeText(GetAttendanceActivity.this,"Attendance Data Received.",Toast.LENGTH_SHORT).show();
                        onAttendanceDataReceived(response1,studentsData);
                    }else{
                        seeAttendance.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(GetAttendanceActivity.this,"Invalid Data Received",Toast.LENGTH_SHORT).show();
                    }

                },
                error -> {
                    seeAttendance.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
//                    loginBtn.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(GetAttendanceActivity.this,"Error Occurred, Try again!",Toast.LENGTH_SHORT).show();
                });

        // Execute API Request
        requestQueue.add(loginRequest);

    }
    StudentAttendanceModel[]  studentAttendanceData;
    private void onAttendanceDataReceived(JSONObject response1, StudentsModel[] studentsData) {

        studentAttendanceData = getStudentsAttendanceData(response1,studentsData);

//        ArrayList<StudentAttendanceModel> ll = new ArrayList<>();
//
//        for(StudentAttendanceModel st:studentAttendanceData){
//            ll.add(st);
//        }

                    seeAttendance.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

        Toast.makeText(GetAttendanceActivity.this,"Generating Attendance Sheet.",Toast.LENGTH_SHORT).show();

//        Intent i = new Intent(GetAttendanceActivity.this,AttendanceReportActivity.class);
//        Bundle args = new Bundle();
//        args.putSerializable("data",(Serializable)ll);
//        i.putExtra("attendanceData",args);
//        startActivity(i);
//        finish();

        downloadBtn.setVisibility(View.VISIBLE);

    }

    private StudentAttendanceModel[] getStudentsAttendanceData(JSONObject response1, StudentsModel[] studentsData) {

        StudentAttendanceModel[] studentsAttendanceData = new StudentAttendanceModel[0];

        try {
            JSONArray receivedData = response1.getJSONArray("students");
            studentsAttendanceData = new StudentAttendanceModel[receivedData.length()];
            for(int i=0;i<receivedData.length();i++){
                String studentId = receivedData.getJSONObject(i).get("studentId").toString();
                JSONObject subjectsData = receivedData.getJSONObject(i).getJSONObject("subjects");

                String branch = branchDropDown.getText().toString();
                String _class = classDropDown.getText().toString();

                if(branch.equals("Information Technology")){
                    branch = "IT";
                }else if(branch.equals("Computer Science")){
                    branch = "CS";
                }

                String subjects[];
                if(branch.equals("CS") && _class.equals("SE")){
                    subjects = getResources().getStringArray(R.array.SE_CS_Subjects);
                }else if((branch.equals("CS") || branch.equals("IT")) && _class.equals("BE")){
                    subjects = getResources().getStringArray(R.array.BE_CS_IT_Subjects);
                }else{
                    subjects = new String[0];
                }

                int attendedLectures[] = new int[subjects.length];


                for(int j=0;j<subjects.length;j++){
                    attendedLectures[j] = Integer.parseInt(subjectsData.get(subjects[j]).toString());
                }

//                attendedLectures[0] = Integer.parseInt(subjectsData.get("Cyber_Security").toString());
//                attendedLectures[1] = Integer.parseInt(subjectsData.get("Soft_Computing").toString());
//                attendedLectures[2] = Integer.parseInt(subjectsData.get("LRPS").toString());
//                attendedLectures[3] = Integer.parseInt(subjectsData.get("Data_Analytics").toString());

                String name = "";
                int rollNo = 0;

                for(StudentsModel st:studentsData){
                    if(st.studentId.equals(studentId)){
                        name = st.name;
                        rollNo = st.rollNo;
                        break;
                    }
                }


                StudentAttendanceModel attendance = new StudentAttendanceModel(rollNo,name,studentId,attendedLectures);
                studentsAttendanceData[i] = attendance;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return studentsAttendanceData;

    }

    private StudentsModel[] getStudentsData(JSONObject response) {

        StudentsModel[] studentsData = new StudentsModel[0];

        try {
            JSONArray receivedData = response.getJSONArray("students");
            studentsData = new StudentsModel[receivedData.length()];
            for(int i=0;i<receivedData.length();i++){
                String studentId = receivedData.getJSONObject(i).get("studentId").toString();
                String name = receivedData.getJSONObject(i).get("name").toString();
                int rollNo = Integer.parseInt(receivedData.getJSONObject(i).get("rollNo").toString());
                StudentsModel student = new StudentsModel(studentId,name,rollNo);
                studentsData[i] = student;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return studentsData;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String designations[] = getResources().getStringArray(R.array.branches);
        String stations[] = getResources().getStringArray(R.array.classes);
        String subjects[] = new String[]{"Select Subject"};

        ArrayAdapter<CharSequence> designationsAdapter = new ArrayAdapter(this, R.layout.dropdown_items, designations);
        branchDropDown.setAdapter(designationsAdapter);

        ArrayAdapter<CharSequence> stationsAdapter = new ArrayAdapter(this, R.layout.dropdown_items, stations);
        classDropDown.setAdapter(stationsAdapter);

        ArrayAdapter<CharSequence> subjectsAdapter = new ArrayAdapter(this, R.layout.dropdown_items, subjects);
        subjectDropDown.setAdapter(subjectsAdapter);
    }

    public void saveAttendanceReport(){

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH_mm_ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        String branch = branchDropDown.getText().toString();
        String _class = classDropDown.getText().toString();

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


        String fileName = _class+ "_" + branch + " " + currentDateandTime + ".xls";

        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.Q){
            String tempPath = Environment.DIRECTORY_DOWNLOADS + "/"+appFolderName+"/"+fileName;
//            Toast.makeText(GetAttendanceActivity.this,tempPath,Toast.LENGTH_SHORT).show();
            filePath = Environment.getExternalStoragePublicDirectory(tempPath);
        } else{
            String tempPath = Environment.getExternalStorageDirectory() +"/"+ Environment.DIRECTORY_DOWNLOADS + "/"+appFolderName+"/"+fileName;
//            Toast.makeText(GetAttendanceActivity.this,tempPath,Toast.LENGTH_SHORT).show();
            filePath = new File(tempPath);
        }

        if(studentAttendanceData.length>0)
        Arrays.sort(studentAttendanceData,(a,b)->a.rollNo-b.rollNo);

        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();

        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Attendance Sheet - " + fileName);

        HSSFRow hssfRow1 = hssfSheet.createRow(0);

        HSSFCell rollNCell = hssfRow1.createCell(0);
        rollNCell.setCellValue("Roll No.");

        HSSFCell cellName1 = hssfRow1.createCell(1);
        cellName1.setCellValue("Name");

        HSSFCell cellStId1 = hssfRow1.createCell(2);
        cellStId1.setCellValue("Student Id");



        String subjects[];
        if(branch.equals("CS") && _class.equals("SE")){
            subjects = getResources().getStringArray(R.array.SE_CS_Subjects);
        }else if((branch.equals("CS") || branch.equals("IT")) && _class.equals("BE")){
            subjects = getResources().getStringArray(R.array.BE_CS_IT_Subjects);
        }else{
            subjects = new String[0];
        }

        for(int i=0;i<subjects.length;i++){
            HSSFCell cellSub1_ = hssfRow1.createCell(3+i);
            cellSub1_.setCellValue(subjects[i]);
        }

//        for(StudentAttendanceModel st:studentAttendanceData)
//        Log.d("sheet",st.toString());

        for(int i=0;i<studentAttendanceData.length;i++){
            HSSFRow hssfRow = hssfSheet.createRow(i+1);

            HSSFCell cellRollNo = hssfRow.createCell(0);
            cellRollNo.setCellValue(studentAttendanceData[i].rollNo+"");

            HSSFCell cellName = hssfRow.createCell(1);
            cellName.setCellValue(studentAttendanceData[i].name);

            HSSFCell cellStId = hssfRow.createCell(2);
            cellStId.setCellValue(studentAttendanceData[i].studentId);

            for(int j=0;j<studentAttendanceData[i].attendedLectures.length;j++){
                HSSFCell cellSub1 = hssfRow.createCell(3+j);
                cellSub1.setCellValue(studentAttendanceData[i].attendedLectures[j]+"");

            }

//            HSSFCell cellSub1 = hssfRow.createCell(3);
//            cellSub1.setCellValue(studentAttendanceData[i].attendedLectures[0]+"");
//
//            HSSFCell cellSub2 = hssfRow.createCell(4);
//            cellSub2.setCellValue(studentAttendanceData[i].attendedLectures[1]+"");
//
//            HSSFCell cellSub3 = hssfRow.createCell(5);
//            cellSub3.setCellValue(studentAttendanceData[i].attendedLectures[2]+"");
//
//            HSSFCell cellSub4 = hssfRow.createCell(6);
//            cellSub4.setCellValue(studentAttendanceData[i].attendedLectures[3]+"");

        }
        hssfSheet.setColumnWidth(1,20*230);
        hssfSheet.setColumnWidth(2,12*220);


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