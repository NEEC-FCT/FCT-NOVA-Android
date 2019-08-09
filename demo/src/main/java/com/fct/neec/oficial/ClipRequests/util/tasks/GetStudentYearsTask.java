package com.fct.neec.oficial.ClipRequests.util.tasks;

import android.content.Context;

import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.exceptions.ServerUnavailableException;
import com.fct.neec.oficial.ClipRequests.util.StudentTools;

public class GetStudentYearsTask extends BaseTask<Object, Void, Student> {

    public interface OnTaskFinishedListener {

        public void onStudentYearsTaskFinished(Student resultCode, int groupPosition);
    }

    private OnTaskFinishedListener mListener;
    private Integer groupPosition;

    public GetStudentYearsTask(Context context, OnTaskFinishedListener listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected Student doInBackground(Object... params) {
        Student student = (Student) params[0];
        groupPosition = (Integer) params[1];
        
        try {
            // Get students years
            return StudentTools.getStudentsYears(mContext, student.getId(), student.getNumberId());
        } catch (ServerUnavailableException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Student result) {
        super.onPostExecute(result);

        if (mListener != null)
            mListener.onStudentYearsTaskFinished(result, groupPosition);
    }
}
