package com.fct.neec.oficial.ClipRequests.util.tasks;

import android.content.Context;
import android.widget.Toast;

import com.fct.neec.oficial.ClipRequests.enums.Result;
import com.fct.neec.oficial.ClipRequests.exceptions.ServerUnavailableException;
import com.fct.neec.oficial.ClipRequests.util.StudentTools;
import com.fct.neec.oficial.R;


public class ConnectClipTask extends BaseTask<String, Void, Result> {

    private OnTaskFinishedListener<Result> mListener;

    public ConnectClipTask(Context context, OnTaskFinishedListener<Result> listener) {
        super(context);
        mListener = listener;
    }

    @Override
    protected Result doInBackground(String... params) {
        // Get user data
        String username = params[0];
        String password = params[1];

        try {
            return StudentTools.signIn(mContext, username, password);
        } catch (ServerUnavailableException e) {
            return Result.OFFLINE;
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);

        switch (result) {
            case OFFLINE:
                //  Toast.makeText(mContext,
                //          mContext.getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
                break;

            case ERROR:
                Toast.makeText(mContext,
                        mContext.getString(R.string.error_fields_incorrect), Toast.LENGTH_SHORT).show();
                break;
        }

        if (mListener != null)
            mListener.onTaskFinished(result);
    }
}