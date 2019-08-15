package com.fct.neec.oficial;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import com.fct.neec.oficial.ClipRequests.entities.Student;
import java.util.Calendar;


/**
 * Implementation of App Widget functionality.
 */
public class ProximaAula extends AppWidgetProvider {

    public static Student data;
    public static RemoteViews views;
    public PendingIntent service = null;



    public static void sethorario(Student data) {
        ProximaAula.data = data;

    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {


        // Construct the RemoteViews object
        if(ProximaAula.data != null){
            ProximaAula.views = new RemoteViews(context.getPackageName(), R.layout.proxima_aula);
            Intent configIntent = new Intent(context, LoadingActivity.class);
            PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
            views.setOnClickPendingIntent(R.id.proxima_aula, configPendingIntent);
        }


        else{
            ProximaAula.views = new RemoteViews(context.getPackageName(), R.layout.sem_aulas);
            Intent configIntent = new Intent(context, LoadingActivity.class);
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
        Log.d("Widget" , "Vou atualizar ");
        alarmManager.setRepeating(AlarmManager.RTC, 5000, 60000 , service);
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

