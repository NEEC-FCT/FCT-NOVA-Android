package com.fct.neec.oficial.ClipRequests.util.tasks;

import android.content.Context;

import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.exceptions.ServerUnavailableException;
import com.fct.neec.oficial.ClipRequests.settings.ClipSettings;
import com.fct.neec.oficial.ClipRequests.util.StudentTools;


public class GetStudentScheduleTask extends BaseTask<Void, Void, Student> {

    private OnTaskFinishedListener<Student> mListener;

    public GetStudentScheduleTask(Context context, OnTaskFinishedListener<Student> listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected Student doInBackground(Void... params) {
        if (ClipSettings.isUserLoggedIn(mContext) && ClipSettings.getYearSelected(mContext) != null) {
            String studentId = ClipSettings.getStudentIdSelected(mContext);
            String year = ClipSettings.getYearSelected(mContext);
            String yearFormatted = ClipSettings.getYearSelectedFormatted(mContext);
            int semester = ClipSettings.getSemesterSelected(mContext);

            //String yearSemesterId = ClipSettings.getStudentYearSemesterIdSelected(mContext);
            String studentNumberId = ClipSettings.getStudentNumberidSelected(mContext);

            // Get student schedule
            try {
                System.out.println("studentId ->" + studentId);
                System.out.println("year ->" + year);
                System.out.println("yearFormatted ->" + yearFormatted);
                System.out.println("semester ->" + semester);

                return StudentTools.getStudentSchedule(mContext, studentId, year, yearFormatted, semester,
                        studentNumberId);
            } catch (ServerUnavailableException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Student result) {
        super.onPostExecute(result);

        if (mListener != null)
            mListener.onTaskFinished(result);
    }

}