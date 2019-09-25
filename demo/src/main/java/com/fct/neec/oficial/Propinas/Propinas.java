package com.fct.neec.oficial.Propinas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.fct.neec.oficial.ClipRequests.entities.StudenPropinas;
import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.settings.ClipSettings;
import com.fct.neec.oficial.ClipRequests.util.tasks.GetStudenPropinas;
import com.fct.neec.oficial.R;
import com.fct.neec.oficial.androidutils.AndroidUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

public class Propinas extends AppCompatActivity implements GetStudenPropinas.OnTaskFinishedListener<Student> , View.OnClickListener {


    private GetStudenPropinas mTask;
    private TableLayout ll;
    private TableLayout table;
    private HashMap<String, StudenPropinas> DB;

    public static HashMap<String, StudenPropinas> getHashMap(Context mContext) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = prefs.getString("MB", "");
        java.lang.reflect.Type type = new TypeToken<HashMap<String, StudenPropinas>>() {
        }.getType();
        HashMap<String, StudenPropinas> obj = gson.fromJson(json, type);
        return obj;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.propinas);
         table = (TableLayout)Propinas.this.findViewById(R.id.attrib_table);
        // Start AsyncTask
        mTask = new GetStudenPropinas(Propinas.this, this);
        AndroidUtils.executeOnPool(mTask);


    }


    @Override
    public void onClick(View v) {

        int clicked_id = v.getId(); // here you get id for clicked TableRow

        System.out.println("ID is --- " + clicked_id);
        String[] array = new TreeSet<>(DB.keySet()).toArray(new String[DB.size()]);
        ClipSettings.saveMBSelected(Propinas.this , array[clicked_id]);
        Intent myIntent = new Intent(Propinas.this, ShowMB.class);
        startActivity(myIntent);

    }


    @Override
    public void onTaskFinished(Student result) {
        //set table title labels


        DB = getHashMap(Propinas.this);
        //Update UI
        int i = 0;
        // Inflate your row "template" and fill out the fields.
        TableRow row = (TableRow) LayoutInflater.from(Propinas.this).inflate(R.layout.attrib_row, null);
        ((TextView)row.findViewById(R.id.Descricao)).setText("Descrição" );
        ((TextView)row.findViewById(R.id.Descricao)).setTypeface(null, Typeface.BOLD);
        ((TextView)row.findViewById(R.id.Montante)).setText( "Montante");
        ((TextView)row.findViewById(R.id.Montante)).setTypeface(null, Typeface.BOLD);
        ((TextView)row.findViewById(R.id.Data)).setText("Data Limite");
        ((TextView)row.findViewById(R.id.Data)).setTypeface(null, Typeface.BOLD);

        table.addView(row);
        Iterator<String> it = new TreeSet<>(DB.keySet()).iterator();
        while (it.hasNext()) {
            String key = it.next();

                // Inflate your row "template" and fill out the fields.
                 row = (TableRow) LayoutInflater.from(Propinas.this).inflate(R.layout.attrib_row, null);
                ((TextView)row.findViewById(R.id.Descricao)).setText( DB.get(key).getNome() );
                ((TextView)row.findViewById(R.id.Montante)).setText(DB.get(key).getMontante());
                  ((TextView)row.findViewById(R.id.Data)).setText(DB.get(key).getData());
                  row.setId(i);
                     row.setOnClickListener(Propinas.this);
                  i++;
                table.addView(row);

            table.requestLayout();     // Not sure if this is neede

        }
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
