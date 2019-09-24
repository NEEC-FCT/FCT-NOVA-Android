package com.fct.neec.oficial.ClipRequests.util.tasks;

import android.content.Context;
import android.util.Log;

import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.exceptions.ServerUnavailableException;
import com.fct.neec.oficial.ClipRequests.settings.ClipSettings;
import com.fct.neec.oficial.ClipRequests.util.StudentTools;

public class GetStudenPropinas extends BaseTask<Void, Void, Student> {

    private OnTaskFinishedListener<Student> mListener;

    public GetStudenPropinas(Context context, OnTaskFinishedListener<Student> listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected Student doInBackground(Void... params) {
        String studentId = ClipSettings.getStudentNumberidSelected(mContext);
        Log.d("Propinas" , "Id is " + studentId);

        String yearFormatted = ClipSettings.getYearSelectedFormatted(mContext);

        // Get student calendar
        try {
             StudentTools.getPropinas(mContext, studentId,
                    yearFormatted);
        } catch (ServerUnavailableException e) {
            return null;
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