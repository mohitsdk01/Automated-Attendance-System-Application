package com.example.automatedattendancesystem;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class studentIdDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Information")
                .setMessage("Your Student Id is: " + StudentRegistrationActivity.studentId)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent myIntent = new Intent(studentIdDialog.this.getActivity(), LoginStudentActivity.class);
                        studentIdDialog.this.startActivity(myIntent);
                        studentIdDialog.this.getActivity().finish();
                    }
                });

        return builder.create();
    }
}