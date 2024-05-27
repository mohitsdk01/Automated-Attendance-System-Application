package com.example.automatedattendancesystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

public class SubjectSelectionActivity extends AppCompatActivity {

    AutoCompleteTextView branchDropDown;
    AutoCompleteTextView classDropDown;
    AutoCompleteTextView subjectDropDown;
    AppCompatButton takeAttendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_selection);

        branchDropDown = findViewById(R.id.dropdown_branch);
        classDropDown = findViewById(R.id.dropdown_class);
        subjectDropDown = findViewById(R.id.dropdown_subject);
        takeAttendance = findViewById(R.id.take_attendance_btn);

        classDropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String branch = branchDropDown.getText().toString();
                String _class = classDropDown.getText().toString();

                if(branch.equals("Information Technology")){
                    branch = "IT";
                }else if(branch.equals("Computer Science")){
                    branch = "CS";
                }

                if(branch.equals("Select Branch")){
                    branchDropDown.setError("Select Branch");
                    return;
                }
                else if(_class.equals("Select Class")){
                    classDropDown.setError("Select Class");
                    return;
                }
                String subjects[] = new String[0];
                if(branch.equals("CS") && _class.equals("SE")){
                     subjects = getResources().getStringArray(R.array.SE_CS_Subjects);
                }else if((branch.equals("CS") || branch.equals("IT")) && _class.equals("BE")){
                    subjects = getResources().getStringArray(R.array.BE_CS_IT_Subjects);
                }

                ArrayAdapter<CharSequence> subjectsAdapter = new ArrayAdapter(SubjectSelectionActivity.this, R.layout.dropdown_items, subjects);
                subjectDropDown.setAdapter(subjectsAdapter);
            }
        });

        takeAttendance.setOnClickListener(new View.OnClickListener() {
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
               else if(subject.equals("Select Subject")){
                    subjectDropDown.setError("Select Subject");
                    return;
                }else{

                    if(branch.equals("Information Technology")){
                        branch = "IT";
                    }else if(branch.equals("Computer Science")){
                        branch = "CS";
                    }

                    Intent i = new Intent(SubjectSelectionActivity.this,TakeAttendanceActivity.class);
                    i.putExtra("Branch",branch);
                    i.putExtra("Class",_class);
                    i.putExtra("Subject",subject);
                    startActivity(i);
                    finish();
                }

            }
        });



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
}