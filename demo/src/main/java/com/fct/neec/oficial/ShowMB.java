package com.fct.neec.oficial;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.settings.ClipSettings;
import com.fct.neec.oficial.ClipRequests.util.tasks.GetStudenPropinas;
import com.fct.neec.oficial.ClipRequests.util.tasks.GetStudentMB;
import com.fct.neec.oficial.androidutils.AndroidUtils;

public class ShowMB extends AppCompatActivity implements GetStudentMB.OnTaskFinishedListener<Student> {


    private GetStudentMB mTask;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.propinas);
        ClipSettings.saveMBSelected(ShowMB.this," /utente/eu/aluno/acto_administrativo/pagamentos_acad%E9micos/propinas/refer%EAncias?tipo_de_per%EDodo_escolar=a&ano_lectivo=2020&institui%E7%E3o=97747&aluno=88508&per%EDodo_escolar=1&presta%E7%E3o=0");

        // Start AsyncTask
        mTask = new GetStudentMB(ShowMB.this, this);
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
