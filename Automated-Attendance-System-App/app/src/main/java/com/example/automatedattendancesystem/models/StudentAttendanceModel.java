package com.example.automatedattendancesystem.models;

import java.util.Arrays;

public class StudentAttendanceModel {

    public int rollNo;
    public String name;
    public String studentId;
    public int[] attendedLectures;

    public StudentAttendanceModel(int rollNo, String name, String studentId, int[] attendedLectures) {
        this.rollNo = rollNo;
        this.name = name;
        this.studentId = studentId;
        this.attendedLectures = attendedLectures;
    }

    @Override
    public String toString() {
        return "StudentAttendanceModel{" +
                "rollNo=" + rollNo +
                ", name='" + name + '\'' +
                ", studentId='" + studentId + '\'' +
                ", attendedLectures=" + Arrays.toString(attendedLectures) +
                '}';
    }
}
