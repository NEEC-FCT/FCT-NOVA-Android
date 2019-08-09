package com.fct.neec.oficial.ClipRequests.util.tasks;

import android.content.Context;

import com.fct.neec.oficial.ClipRequests.entities.User;
import com.fct.neec.oficial.ClipRequests.settings.ClipSettings;
import com.fct.neec.oficial.ClipRequests.util.StudentTools;

public class GetStudentNumbersTask extends BaseTask<Void, Void, User> {

    public interface OnTaskFinishedListener {

        public void onStudentNumbersTaskFinished(User result);
    }

    private OnTaskFinishedListener mListener;

    public GetStudentNumbersTask(Context context, OnTaskFinishedListener listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected User doInBackground(Void... params) {
        long userId = ClipSettings.getLoggedInUserId(mContext);

        // Get students numbers
        return StudentTools.getStudents(mContext, userId);
    }

    @Override
    protected void onPostExecute(User result) {
        super.onPostExecute(result);

        if (mListener != null)
            mListener.onStudentNumbersTaskFinished(result);
    }
}
