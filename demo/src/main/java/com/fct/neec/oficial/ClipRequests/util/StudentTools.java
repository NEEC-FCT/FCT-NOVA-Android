package com.fct.neec.oficial.ClipRequests.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Toast;


import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.entities.User;
import com.fct.neec.oficial.ClipRequests.enums.Result;
import com.fct.neec.oficial.ClipRequests.exceptions.ServerUnavailableException;
import com.fct.neec.oficial.ClipRequests.network.StudentCalendarRequest;
import com.fct.neec.oficial.ClipRequests.network.StudentClassesDocsRequest;
import com.fct.neec.oficial.ClipRequests.network.StudentClassesRequest;
import com.fct.neec.oficial.ClipRequests.network.StudentRequest;
import com.fct.neec.oficial.ClipRequests.network.StudentScheduleRequest;
import com.fct.neec.oficial.ClipRequests.settings.ClipSettings;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentTools {

    public static Result signIn(Context mContext, String username, String password)
            throws ServerUnavailableException {

        // Check for connectivity


        // Sign in the user, and returns Students available
        User user = StudentRequest.signIn(mContext, username, password);

        // Invalid credentials
        if(! user.hasStudents())
            return Result.ERROR;

        long userId = DBUtils.getUserId(mContext, username);

        // If the user doesn't exist, create a new one
        if(userId == -1) {
            userId = DBUtils.createUser(mContext, username);

            // Insert Students
            DBUtils.insertStudentsNumbers(mContext, userId, user);
        }

        // User is now logged in
        ClipSettings.setLoggedInUser(mContext, userId, username, password);

        return Result.SUCCESS;
    }

    /*
     * ////////////////////////////// STUDENT, STUDENTYEARS, STUDENTNUMBERS  //////////////////////////////
     */

    public static User getStudents(Context mContext, long userId) {

        return DBUtils.getStudents(mContext, userId);
    }

    public static Student getStudentsYears(Context mContext, String studentId, String studentNumberId)
            throws ServerUnavailableException {

        Student student = DBUtils.getStudentYears(mContext, studentId);

        
        Log.d("CLIP" ,"has " + student.hasStudentYears());

        if(student.hasStudentYears())
            return student;

        // Check for connectivity


        // Get student years from the server
        student = StudentRequest.getStudentsYears(mContext, studentNumberId);

        // Insert Students
        DBUtils.insertStudentYears(mContext, studentId, student);

        return student;
    }

    public static User updateStudentNumbersAndYears(Context mContext, long userId)
            throws ServerUnavailableException {

        Log.d("CLIP" ,"request!");

        // Get (new) studentsNumbers from the server
        User user = StudentRequest.getStudentsNumbers(mContext);

        Log.d("CLIP" ,"deleting!");

        // Delete studentsNumbers and studentsYears
        DBUtils.deleteStudentsNumbers(mContext, userId);

        Log.d("CLIP" ,"inserting!");

        // Insert Students
        DBUtils.insertStudentsNumbers(mContext, userId, user);

        return user;
    }

    public static Student updateStudentPage(Context mContext, String studentId, String studentNumberId,
                                            String studentYearSemesterId)
            throws ServerUnavailableException {

        Log.d("CLIP" ,"request!");

        // Get (new) student info from the server
        Student student = StudentRequest.getStudentsYears(mContext, studentNumberId);

        Log.d("CLIP" ,"deleting!");

        // Delete students info
        DBUtils.deleteStudentsInfo(mContext, studentYearSemesterId);

        Log.d("CLIP" ,"inserting!");

        // Insert students info
        DBUtils.insertStudentYears(mContext, studentId, student);

        return student;
    }

    /*
     * ////////////////////////////// STUDENT SCHEDULE  //////////////////////////////
     */


    public static Student getStudentSchedule(Context mContext, String studentId, String year, String yearFormatted,
                                             int semester, String studentNumberId)
            throws ServerUnavailableException {

        // First, we get the yearSemesterId
        String yearSemesterId = DBUtils.getYearSemesterId(mContext, studentId, year, semester);

        Student student = DBUtils.getStudentSchedule(mContext, yearSemesterId);

        Log.d("CLIP" ,"has " + (student != null));

        if(student != null)
            return student;


        // Check for connectivity


        // Get student schedule from the server
        student = StudentScheduleRequest.getSchedule(mContext, studentNumberId, yearFormatted, semester);

        Log.d("CLIP" ,"schedule request done!");

        // Insert schedule on database
        DBUtils.insertStudentSchedule(mContext, yearSemesterId, student);

        Log.d("CLIP" ,"schedule inserted!");

        return student;
    }

    /*
     * ////////////////////////////// STUDENT CLASSES  //////////////////////////////
     */


    public static Student getStudentClasses(Context mContext, String studentId, String year, String yearFormatted,
                                             int semester, String studentNumberId)
            throws ServerUnavailableException {

        // First, we get the yearSemesterId
        String yearSemesterId = DBUtils.getYearSemesterId(mContext, studentId, year, semester);

        Student student = DBUtils.getStudentClasses(mContext, yearSemesterId);

        Log.d("CLIP" ,"has " + (student != null));

        if(student != null)
            return student;


        // Check for connectivity


        // Get student classes from the server
        student = StudentClassesRequest.getClasses(mContext, studentNumberId, yearFormatted);

        Log.d("CLIP" ,"classes request done!");

        // Insert classes on database
        DBUtils.insertStudentClasses(mContext, yearSemesterId, student);

        Log.d("CLIP" ,"classes inserted!");

        return student;
    }

    public static Student getStudentClassesDocs(Context mContext, String studentClassId, String yearFormatted,
                                            int semester, String studentNumberId, String studentClassSelected,
                                            String docType)
            throws ServerUnavailableException {

        Student student = DBUtils.getStudentClassesDocs(mContext, studentClassId, docType);

        Log.d("CLIP" ,"has " + (student != null));

        if(student != null)
            return student;


        // Check for connectivity


        // Get student classes docs from the server
        student = StudentClassesDocsRequest.getClassesDocs(mContext, studentNumberId,
                yearFormatted, semester, studentClassSelected, docType);

        Log.d("CLIP" ,"classes docs request done!");

        // Insert classes docs on database
        DBUtils.insertStudentClassesDocs(mContext, studentClassId, student);

        Log.d("CLIP" ,"classes docs inserted!");

        return student;
    }

    /*
     * ////////////////////////////// STUDENT CALENDAR  //////////////////////////////
     */

    public static Student getStudentCalendar(Context mContext, String studentId, String year, String yearFormatted,
                                             int semester, String studentNumberId)
            throws ServerUnavailableException {

        // First, we get the yearSemesterId
        String yearSemesterId = DBUtils.getYearSemesterId(mContext, studentId, year, semester);

        Student student = DBUtils.getStudentCalendar(mContext, yearSemesterId);

        Log.d("CLIP" ,"has " + (student != null));

        if(student != null)
            return student;


        // Check for connectivity


        // ---- EXAM CALENDAR ----

        // Get student exam calendar from the server
        student = new Student();
        StudentCalendarRequest.getExamCalendar(mContext, student, studentNumberId, yearFormatted, semester);

        // ---- TEST CALENDAR ----

        // Get student test calendar from the server
        StudentCalendarRequest.getTestCalendar(mContext, student, studentNumberId, yearFormatted, semester);

        Log.d("CLIP" ,"calendar request done!");

        // Insert calendar on database
        DBUtils.insertStudentCalendar(mContext, yearSemesterId, student);

        Log.d("CLIP" ,"calendar inserted!");

        return student;
    }





}
