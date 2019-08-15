package com.fct.neec.oficial.ClipRequests.util.tasks;

import android.content.Context;
import android.os.AsyncTask;


public abstract class BaseTask<A, B, C> extends AsyncTask<A, B, C> {

    protected Context mContext;

    public BaseTask(Context context) {
        mContext = context;
    }

    @Override
    protected void onPostExecute(C result) {
        super.onPostExecute(result);

        // Server is unavailable right now
        //  if(result == null)

    }

    public interface OnTaskFinishedListener<C> {

        public void onTaskFinished(C result);
    }

    public interface OnUpdateTaskFinishedListener<C> {

        public void onUpdateTaskFinished(C result);
    }

}