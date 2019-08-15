package com.fct.neec.oficial.ClipRequests.util.tasks;

import android.content.Context;

import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.exceptions.ServerUnavailableException;
import com.fct.neec.oficial.ClipRequests.settings.ClipSettings;
import com.fct.neec.oficial.ClipRequests.util.StudentTools;
import com.fct.neec.oficial.R;

public class GetStudentClassesDocsTask extends BaseTask<Integer, Void, Student> {

    private Integer groupPosition;
    private OnTaskFinishedListener mListener;
    public GetStudentClassesDocsTask(Context context, OnTaskFinishedListener listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected Student doInBackground(Integer... params) {
        groupPosition = params[0];

        //String studentId = ClipSettings.getStudentIdSelected(mContext);
        String yearFormatted = ClipSettings.getYearSelectedFormatted(mContext);
        int semester = ClipSettings.getSemesterSelected(mContext);
        String studentNumberId = ClipSettings.getStudentNumberidSelected(mContext);
        String studentClassIdSelected = ClipSettings.getStudentClassIdSelected(mContext);
        String studentClassSelected = ClipSettings.getStudentClassSelected(mContext);
        String docType = mContext.getResources()
                .getStringArray(R.array.classes_docs_type_array)[groupPosition];

        /*System.out.println("DOINBACKGROUND -> studentID" + studentId + ", year:" + year
                + ", semester:" + semester
                + ", studentNumberID:" + studentNumberId);*/

        // Get student class docs
        try {
            return StudentTools.getStudentClassesDocs(mContext, studentClassIdSelected, yearFormatted,
                    semester, studentNumberId, studentClassSelected, docType);
        } catch (ServerUnavailableException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Student result) {
        super.onPostExecute(result);

        if (mListener != null)
            mListener.onTaskFinished(result, groupPosition);
    }

    public interface OnTaskFinishedListener {

        public void onTaskFinished(Student result, int groupPosition);
    }

}