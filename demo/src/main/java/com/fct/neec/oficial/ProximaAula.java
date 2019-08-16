package com.fct.neec.oficial;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.google.gson.Gson;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


/**
 * Implementation of App Widget functionality.
 */
public class ProximaAula extends AppWidgetProvider {

    public static final String KEY_CONNECTIONS = "KEY_CONNECTIONS";
    public static Student data;
    public static RemoteViews views;
    public static SharedPreferences mPrefs;
    public PendingIntent service = null;

    public static Student ReadState() {
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        data = gson.fromJson(json, Student.class);
        return data;

    }

    public static void SaveState() {

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        prefsEditor.putString("MyObject", json);
        prefsEditor.commit();
    }

    public static void sethorario(Student data) {
        ProximaAula.data = data;

    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {

        mPrefs = context.getSharedPreferences("Teste", MODE_PRIVATE);

        // Construct the RemoteViews object
        ReadState();
        if (data != null) {
            views = new RemoteViews(context.getPackageName(), R.layout.proxima_aula);
            Intent configIntent = new Intent(context, MainActivity.class);
            PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
            views.setOnClickPendingIntent(R.id.proxima_aula, configPendingIntent);
        } else {
            views = new RemoteViews(context.getPackageName(), R.layout.sem_aulas);
            Intent configIntent = new Intent(context, MainActivity.class);
            PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
            views.setOnClickPendingIntent(R.id.sem_aula, configPendingIntent);
        }


        //set alarm
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Calendar TIME = Calendar.getInstance();
        TIME.set(Calendar.MINUTE, 0);
        TIME.set(Calendar.SECOND, 0);
        TIME.set(Calendar.MILLISECOND, 0);
        final Intent in = new Intent(context, MyService.class);
        if (service == null) {
            service = PendingIntent.getService(context, 0, in, PendingIntent.FLAG_CANCEL_CURRENT);
        }
        Log.d("Widget", "Vou atualizar ");
        alarmManager.setRepeating(AlarmManager.RTC, 5000, 60000, service);
        //onclick refresh
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

