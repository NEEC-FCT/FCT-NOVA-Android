package com.fct.neec.oficial.ClipRequests.util.tasks;

import android.content.Context;

import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.exceptions.ServerUnavailableException;
import com.fct.neec.oficial.ClipRequests.settings.ClipSettings;
import com.fct.neec.oficial.ClipRequests.util.StudentTools;

public class UpdateStudentPageTask extends BaseTask<Void, Void, Student> {

    private OnUpdateTaskFinishedListener<Student> mListener;

    public UpdateStudentPageTask(Context context, OnUpdateTaskFinishedListener<Student> listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected Student doInBackground(Void... params) {
        String studentId = ClipSettings.getStudentIdSelected(mContext);
        String studentNumberId = ClipSettings.getStudentNumberidSelected(mContext);
        String studentYearSemesterId = ClipSettings.getStudentYearSemesterIdSelected(mContext);

        try {
            // Update students info
            return StudentTools.updateStudentPage(mContext, studentId, studentNumberId, studentYearSemesterId);
        } catch (ServerUnavailableException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Student result) {
        super.onPostExecute(result);

        if (mListener != null)
            mListener.onUpdateTaskFinished(result);
    }
}
