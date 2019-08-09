package com.fct.neec.oficial.ClipRequests.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.fct.neec.oficial.ClipRequests.provider.ClipMobileContract.ScheduleClassesColumns;
import com.fct.neec.oficial.ClipRequests.provider.ClipMobileContract.ScheduleDaysColumns;
import com.fct.neec.oficial.ClipRequests.provider.ClipMobileContract.StudentClassesColumns;
import com.fct.neec.oficial.ClipRequests.provider.ClipMobileContract.StudentsColumns;
import com.fct.neec.oficial.ClipRequests.provider.ClipMobileContract.StudentsYearSemesterColumns;

import static com.fct.neec.oficial.ClipRequests.provider.ClipMobileContract.*;


public class ClipMobileDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "clip_mobile_database";
    public static final int DATABASE_VERSION = 2;

    private DatabaseUtils.InsertHelper mUsersInserter;
    private DatabaseUtils.InsertHelper mStudentsInserter;
    private DatabaseUtils.InsertHelper mStudentsYearSemesterInserter;
    private DatabaseUtils.InsertHelper mScheduleDaysInserter;
    private DatabaseUtils.InsertHelper mScheduleClassesInserter;
    private DatabaseUtils.InsertHelper mStudentClassesInserter;
    private DatabaseUtils.InsertHelper mStudentClassesDocsInserter;
    private DatabaseUtils.InsertHelper mStudentCalendarInserter;

    public interface Tables {
        String USERS = "users";
        String STUDENTS = "students";
        String STUDENTS_YEAR_SEMESTER = "students_year_semester";
        String SCHEDULE_DAYS = "schedule_days";
        String SCHEDULE_CLASSES = "schedule_classes";
        String STUDENT_CLASSES = "student_classes";
        String STUDENT_CLASSES_DOCS = "student_classes_docs";
        String STUDENT_CALENDAR = "student_calendar";
    }

    interface References {

        String USER_ID = "REFERENCES " + Tables.USERS +
                "(" + BaseColumns._ID + ")";

        String STUDENT_ID = "REFERENCES " + Tables.STUDENTS +
                "(" + BaseColumns._ID + ")";

        String STUDENTS_YEAR_SEMESTER_ID = "REFERENCES " + Tables.STUDENTS_YEAR_SEMESTER +
                "(" + BaseColumns._ID + ")";

        String STUDENT_SCHEDULE_DAY_ID = "REFERENCES " + Tables.SCHEDULE_DAYS +
                "(" + BaseColumns._ID + ")";

        String STUDENT_CLASSES_ID = "REFERENCES " + Tables.STUDENT_CLASSES +
                "(" + BaseColumns._ID + ")";
    }

    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + Tables.USERS
            + " ("

            + BaseColumns._ID + " INTEGER PRIMARY KEY,"

            + UsersColumns.USERNAME + " TEXT NOT NULL,"

            + "UNIQUE (" + UsersColumns.USERNAME + ")"

            + ");";

    private static final String CREATE_STUDENTS_TABLE = "CREATE TABLE " + Tables.STUDENTS
            + " ("

            + BaseColumns._ID + " INTEGER PRIMARY KEY,"

            + UsersColumns.REF_USERS_ID + " TEXT " + References.USER_ID + " ON DELETE CASCADE,"

            + StudentsColumns.NUMBER_ID + " TEXT NOT NULL,"

            + StudentsColumns.NUMBER + " TEXT NOT NULL"

            + ");";

    private static final String CREATE_STUDENTS_YEAR_SEMESTER_TABLE = "CREATE TABLE " + Tables.STUDENTS_YEAR_SEMESTER
            + " ("

            + BaseColumns._ID + " INTEGER PRIMARY KEY,"

            + StudentsColumns.REF_STUDENTS_ID + " TEXT " + References.STUDENT_ID + " ON DELETE CASCADE,"

            + StudentsYearSemesterColumns.YEAR + " TEXT NOT NULL,"

            + StudentsYearSemesterColumns.SEMESTER + " TEXT NOT NULL"

            + ");";

    private static final String CREATE_SCHEDULE_DAYS_TABLE = "CREATE TABLE " + Tables.SCHEDULE_DAYS
            + " ("

            + BaseColumns._ID + " INTEGER PRIMARY KEY,"

            + StudentsYearSemesterColumns.REF_STUDENTS_YEAR_SEMESTER_ID + " TEXT " +
                References.STUDENTS_YEAR_SEMESTER_ID + " ON DELETE CASCADE,"

            + ScheduleDaysColumns.DAY + " TEXT NOT NULL"

            + ");";

    private static final String CREATE_SCHEDULE_CLASSES_TABLE = "CREATE TABLE " + Tables.SCHEDULE_CLASSES
            + " ("

            + BaseColumns._ID + " INTEGER PRIMARY KEY,"

            + ScheduleDaysColumns.REF_SCHEDULE_DAYS_ID + " TEXT " +
                References.STUDENT_SCHEDULE_DAY_ID + " ON DELETE CASCADE,"

            + ScheduleClassesColumns.NAME + " TEXT NOT NULL,"

            + ScheduleClassesColumns.NAME_ABBREVIATION + " TEXT NOT NULL,"

            + ScheduleClassesColumns.TYPE + " TEXT NOT NULL,"

            + ScheduleClassesColumns.HOUR_START + " TEXT NOT NULL,"

            + ScheduleClassesColumns.HOUR_END + " TEXT NOT NULL,"

            + ScheduleClassesColumns.ROOM + " TEXT"

            + ");";

    private static final String CREATE_STUDENT_CLASSES_TABLE = "CREATE TABLE " + Tables.STUDENT_CLASSES
            + " ("

            + BaseColumns._ID + " INTEGER PRIMARY KEY,"

            + StudentsYearSemesterColumns.REF_STUDENTS_YEAR_SEMESTER_ID + " TEXT " +
                References.STUDENTS_YEAR_SEMESTER_ID + " ON DELETE CASCADE,"

            + StudentClassesColumns.NAME + " TEXT NOT NULL,"

            + StudentClassesColumns.NUMBER + " TEXT NOT NULL,"

            + StudentClassesColumns.SEMESTER + " TEXT NOT NULL"

            + ");";

    private static final String CREATE_STUDENT_CLASSES_DOCS_TABLE = "CREATE TABLE " + Tables.STUDENT_CLASSES_DOCS
            + " ("

            + BaseColumns._ID + " INTEGER PRIMARY KEY,"

            + StudentClassesColumns.REF_STUDENT_CLASSES_ID + " TEXT " +
                References.STUDENT_CLASSES_ID + " ON DELETE CASCADE,"

            + StudentClassesDocsColumns.NAME + " TEXT NOT NULL,"

            + StudentClassesDocsColumns.URL + " TEXT NOT NULL,"

            + StudentClassesDocsColumns.DATE + " TEXT NOT NULL,"

            + StudentClassesDocsColumns.SIZE + " TEXT NOT NULL,"

            + StudentClassesDocsColumns.TYPE + " TEXT NOT NULL"

            + ");";

    private static final String CREATE_STUDENT_CALENDAR_TABLE = "CREATE TABLE " + Tables.STUDENT_CALENDAR
            + " ("

            + BaseColumns._ID + " INTEGER PRIMARY KEY,"

            + StudentsYearSemesterColumns.REF_STUDENTS_YEAR_SEMESTER_ID + " TEXT " +
                References.STUDENTS_YEAR_SEMESTER_ID + " ON DELETE CASCADE,"

            + StudentCalendarColumns.IS_EXAM + " INTEGER,"

            + StudentCalendarColumns.NAME + " TEXT NOT NULL,"

            + StudentCalendarColumns.DATE + " TEXT NOT NULL,"

            + StudentCalendarColumns.HOUR + " TEXT NOT NULL,"

            + StudentCalendarColumns.ROOMS + " TEXT,"

            + StudentCalendarColumns.NUMBER + " TEXT"

            + ");";



    public ClipMobileDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public long insertUsers(ContentValues values) {
        return mUsersInserter.insert(values);
    }

    public long insertStudents(ContentValues values) {
        return mStudentsInserter.insert(values);
    }

    public long insertStudentYears(ContentValues values) {
        return mStudentsYearSemesterInserter.insert(values);
    }

    public long insertScheduleDays(ContentValues values) {
        return mScheduleDaysInserter.insert(values);
    }

    public long insertScheduleClasses(ContentValues values) {
        return mScheduleClassesInserter.insert(values);
    }

    public long insertStudentClasses(ContentValues values) {
        return mStudentClassesInserter.insert(values);
    }

    public long insertStudentClassesDocs(ContentValues values) {
        return mStudentClassesDocsInserter.insert(values);
    }

    public long insertStudentCalendar(ContentValues values) {
        return mStudentCalendarInserter.insert(values);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_STUDENTS_TABLE);
        db.execSQL(CREATE_STUDENTS_YEAR_SEMESTER_TABLE);
        db.execSQL(CREATE_SCHEDULE_DAYS_TABLE);
        db.execSQL(CREATE_SCHEDULE_CLASSES_TABLE);
        db.execSQL(CREATE_STUDENT_CLASSES_TABLE);
        db.execSQL(CREATE_STUDENT_CLASSES_DOCS_TABLE);
        db.execSQL(CREATE_STUDENT_CALENDAR_TABLE);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }

        mUsersInserter = new DatabaseUtils.InsertHelper(db, Tables.USERS);
        mStudentsInserter = new DatabaseUtils.InsertHelper(db, Tables.STUDENTS);
        mStudentsYearSemesterInserter = new DatabaseUtils.InsertHelper(db, Tables.STUDENTS_YEAR_SEMESTER);
        mScheduleDaysInserter = new DatabaseUtils.InsertHelper(db, Tables.SCHEDULE_DAYS);
        mScheduleClassesInserter = new DatabaseUtils.InsertHelper(db, Tables.SCHEDULE_CLASSES);
        mStudentClassesInserter = new DatabaseUtils.InsertHelper(db, Tables.STUDENT_CLASSES);
        mStudentClassesDocsInserter = new DatabaseUtils.InsertHelper(db, Tables.STUDENT_CLASSES_DOCS);
        mStudentCalendarInserter = new DatabaseUtils.InsertHelper(db, Tables.STUDENT_CALENDAR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d( "CLIP" , "ClipMobileDatabse - onUpgrade from " + oldVersion + " to " + newVersion);
        
        switch(oldVersion) {
            case 1:
                upgradeToSecond(db);
        }
        
    }

    private void upgradeToSecond(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL("DROP TABLE IF EXISTS " + Tables.STUDENTS_YEAR_SEMESTER);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.SCHEDULE_DAYS);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.SCHEDULE_CLASSES);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.STUDENT_CLASSES);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.STUDENT_CLASSES_DOCS);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.STUDENT_CALENDAR);
            
            db.execSQL(CREATE_STUDENTS_YEAR_SEMESTER_TABLE);
            db.execSQL(CREATE_SCHEDULE_DAYS_TABLE);
            db.execSQL(CREATE_SCHEDULE_CLASSES_TABLE);
            db.execSQL(CREATE_STUDENT_CLASSES_TABLE);
            db.execSQL(CREATE_STUDENT_CLASSES_DOCS_TABLE);
            db.execSQL(CREATE_STUDENT_CALENDAR_TABLE);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
