package com.fct.neec.oficial.ClipRequests.util.tasks;

import android.content.Context;

import com.fct.neec.oficial.ClipRequests.entities.User;
import com.fct.neec.oficial.ClipRequests.exceptions.ServerUnavailableException;
import com.fct.neec.oficial.ClipRequests.settings.ClipSettings;
import com.fct.neec.oficial.ClipRequests.util.StudentTools;

public class UpdateStudentNumbersTask extends BaseTask<Void, Void, User> {
    
    private OnUpdateTaskFinishedListener<User> mListener;

    public UpdateStudentNumbersTask(Context context, OnUpdateTaskFinishedListener<User> listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected User doInBackground(Void... params) {
        long userId = ClipSettings.getLoggedInUserId(mContext);

        System.out.println("UPDATE STUDENT NUMBERS TASK userId:: " + userId);

        try {
            // Update students numbers and years
            return StudentTools.updateStudentNumbersAndYears(mContext, userId);
        } catch (ServerUnavailableException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(User result) {
        super.onPostExecute(result);

        if (mListener != null)
            mListener.onUpdateTaskFinished(result);
    }
}
