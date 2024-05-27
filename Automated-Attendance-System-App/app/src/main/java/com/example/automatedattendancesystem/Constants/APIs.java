package com.example.automatedattendancesystem.Constants;

public class APIs {

    public static final String SERVER_URL = "https://automated-attendance-system-api.onrender.com/api/";

    public static final String SIGN_UP_URL = SERVER_URL + "student/signup";
    public static final String SIGN_IN_URL = SERVER_URL + "student/login";
    public static final String GET_STUDENT_NAMES_FROM_STUDENT_ID_URL = SERVER_URL + "student/getStudentNamesFromMac";
    public static final String MARK_ATTENDANCE_URL = SERVER_URL + "attendance/markAttendance";
    public static final String GET_ALL_STUDENT_DATA = SERVER_URL + "student/getAllStudents";
    public static final String GET_ATTENDANCE_URL = SERVER_URL + "attendance/getAttendance";
}