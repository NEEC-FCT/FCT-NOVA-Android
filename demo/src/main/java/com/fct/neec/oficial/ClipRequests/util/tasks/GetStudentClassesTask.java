package com.fct.neec.oficial.ClipRequests.util.tasks;

import android.content.Context;

import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.exceptions.ServerUnavailableException;
import com.fct.neec.oficial.ClipRequests.settings.ClipSettings;
import com.fct.neec.oficial.ClipRequests.util.StudentTools;

public class GetStudentClassesTask extends BaseTask<Void, Void, Student> {

    private OnTaskFinishedListener<Student> mListener;

    public GetStudentClassesTask(Context context, OnTaskFinishedListener<Student> listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected Student doInBackground(Void... params) {
        try {

            String studentId = ClipSettings.getStudentIdSelected(mContext);
            String year = ClipSettings.getYearSelected(mContext);
            String yearFormatted = ClipSettings.getYearSelectedFormatted(mContext);
            int semester = ClipSettings.getSemesterSelected(mContext);
            String studentNumberId = ClipSettings.getStudentNumberidSelected(mContext);

            // Get student classes
            try {
                return StudentTools.getStudentClasses(mContext, studentId, year, yearFormatted, semester, studentNumberId);
            } catch (ServerUnavailableException e) {
                return null;
            }
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Student result) {
        super.onPostExecute(result);

        if (mListener != null)
            mListener.onTaskFinished(result);
    }
}