package com.example.automatedattendancesystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.automatedattendancesystem.models.StudentAttendanceModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class AttendanceReportActivity extends AppCompatActivity {

    private File filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/demo.xls");
    Button downloadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_report);

        downloadBtn = findViewById(R.id.download);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("data");
        ArrayList<StudentAttendanceModel> studentAttendanceModels = (ArrayList<StudentAttendanceModel>) args.getSerializable("attendanceData");

        for(StudentAttendanceModel st:studentAttendanceModels){
            Log.d("checking",st.toString());
        }

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAttendanceReport();
            }
        });

    }

    public void saveAttendanceReport(){
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Attendance Sheet");


        HSSFRow hssfRow = hssfSheet.createRow(0);
        HSSFCell hssfCell = hssfRow.createCell(0);
        hssfCell.setCellValue("Hello");

        HSSFCell hssfCell1 = hssfRow.createCell(1);
        hssfCell1.setCellValue("World");


        try {
            if (!filePath.exists()){
                filePath.createNewFile();
            }


            FileOutputStream fileOutputStream= new FileOutputStream(filePath);
            hssfWorkbook.write(fileOutputStream);
            Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show();

            if (fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}