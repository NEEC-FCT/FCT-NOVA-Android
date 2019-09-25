package com.fct.neec.oficial.Propinas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fct.neec.oficial.ClipRequests.entities.StudenPropinas;
import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.settings.ClipSettings;
import com.fct.neec.oficial.ClipRequests.util.tasks.GetStudentMB;
import com.fct.neec.oficial.R;
import com.fct.neec.oficial.androidutils.AndroidUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class ShowMB extends AppCompatActivity implements GetStudentMB.OnTaskFinishedListener<Student> {


    private GetStudentMB mTask;
    private HashMap<String, StudenPropinas> DB;
    private TextView descricao;
    private TextView entidade;
    private TextView referencia;
    private TextView montante;
    private TextView data;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.showmb);

         descricao = findViewById(R.id.descricao);
         entidade =  findViewById(R.id.entidade);
         referencia = findViewById(R.id.referencia);
         montante = findViewById(R.id.montante);
         data = findViewById(R.id.data);


        // Start AsyncTask
        mTask = new GetStudentMB(ShowMB.this, this);
        AndroidUtils.executeOnPool(mTask);



    }

    @Override
    public void onTaskFinished(Student result) {


        DB = getHashMap(ShowMB.this);
        String selected = ClipSettings.getSelectedMBURL(ShowMB.this);
        StudenPropinas info = DB.get(selected);
        descricao.setText("DESCRIÇÃO                        " + info.getNome());
        entidade.setText("ENTIDADE                        " + info.getEntidade());
        referencia.setText("REFERÊNCIA                    " + info.getReferencia());
        montante.setText("MONTANTE                        " + info.getMontante() );
        data.setText("DATA LIMITE                        " + info.getData());

    }


    public static HashMap<String, StudenPropinas> getHashMap(Context mContext) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = prefs.getString("MB","");
        java.lang.reflect.Type type = new TypeToken<HashMap<String,StudenPropinas>>(){}.getType();
        HashMap<String,StudenPropinas> obj = gson.fromJson(json, type);
        return obj;
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
