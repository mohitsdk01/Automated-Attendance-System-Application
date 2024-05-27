package com.example.automatedattendancesystem.models;

public class StudentsModel {

    public String studentId;
    public String name;
    public int rollNo;

    public StudentsModel(String studentId, String name, int rollNo) {
        this.studentId = studentId;
        this.name = name;
        this.rollNo = rollNo;
    }

    @Override
    public String toString() {
        return "StudentsModel{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", rollNo=" + rollNo +
                '}';
    }
}
