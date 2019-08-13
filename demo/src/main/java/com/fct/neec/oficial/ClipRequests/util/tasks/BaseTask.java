package com.fct.neec.oficial.ClipRequests.util.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.fct.neec.oficial.R;


public abstract class BaseTask<A,B,C> extends AsyncTask<A, B, C> {

    public interface OnTaskFinishedListener<C> {

        public void onTaskFinished(C result);
    }

    public interface OnUpdateTaskFinishedListener<C> {

        public void onUpdateTaskFinished(C result);
    }

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
    
}