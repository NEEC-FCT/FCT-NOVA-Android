package com.fct.neec.oficial.ClipRequests.util.tasks;

import android.content.Context;

import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.exceptions.ServerUnavailableException;
import com.fct.neec.oficial.ClipRequests.settings.ClipSettings;
import com.fct.neec.oficial.ClipRequests.util.StudentTools;

public class GetStudentMB extends BaseTask<Void, Void, Student> {

    private OnTaskFinishedListener<Student> mListener;

    public GetStudentMB(Context context, OnTaskFinishedListener<Student> listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected Student doInBackground(Void... params) {
        try {

            String url = ClipSettings.getSelectedMBURL(mContext);

            // Get student MB
            try {
                StudentTools.getReferenciasMB(mContext, url);
                return  null;
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