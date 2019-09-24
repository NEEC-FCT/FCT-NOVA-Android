package com.fct.neec.oficial;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.util.tasks.GetStudenPropinas;
import com.fct.neec.oficial.androidutils.AndroidUtils;

public class Propinas extends AppCompatActivity implements GetStudenPropinas.OnTaskFinishedListener<Student> {


    private GetStudenPropinas mTask;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.propinas);

        // Start AsyncTask
        mTask = new GetStudenPropinas(Propinas.this, this);
        AndroidUtils.executeOnPool(mTask);


    }

    @Override
    public void onTaskFinished(Student result) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        cancelTasks(mTask);
    }

    protected void cancelTasks(AsyncTask mTask) {
        if (mTask != null && mTask.getStatus() != AsyncTask.Status.FINISHED)
            mTask.cancel(true);
    }
}
