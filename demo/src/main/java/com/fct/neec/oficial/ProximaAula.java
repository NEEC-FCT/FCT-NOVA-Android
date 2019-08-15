package com.fct.neec.oficial;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.entities.StudentScheduleClass;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Implementation of App Widget functionality.
 */
public class ProximaAula extends AppWidgetProvider {

    public static Student data;
    public static RemoteViews views;
    private static StudentScheduleClass melhor;
    public PendingIntent service = null;

    private void updateUI() {
        if(data != null) {
            Map<Integer, List<StudentScheduleClass>> horario = data.getScheduleClasses();
            Calendar c = Calendar.getInstance();
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            int diff = Integer.MAX_VALUE;

            if (horario.containsKey(dayOfWeek)) {
                //Vamos mostrar
                List<StudentScheduleClass> diaAtual = horario.get(dayOfWeek);
                Iterator<StudentScheduleClass> it = diaAtual.iterator();
                int i = 0;
                while (it.hasNext()) {
                    StudentScheduleClass aula = it.next();
                    i++;
                    Log.d("Widget", aula.getName() + " " + aula.getHourStart() + " diff: " + diffHoras(hour + ":" + minute, aula.getHourStart()));
                    int distancia = diffHoras(hour + ":" + minute, aula.getHourStart());
                    if (distancia <= diff) {
                        diff = distancia;
                        melhor = aula;
                    }
                }
            }
        }
    }

    private static int diffHoras(String timeString1, String timeString2) {

        String[] fractions1 = timeString1.split(":");
        String[] fractions2 = timeString2.split(":");
        Integer hours1 = Integer.parseInt(fractions1[0]);
        Integer hours2 = Integer.parseInt(fractions2[0]);
        Integer minutes1 = Integer.parseInt(fractions1[1]);
        Integer minutes2 = Integer.parseInt(fractions2[1]);
        int hourDiff = hours1 - hours2;
        int minutesDiff = minutes1 - minutes2;
        if (minutesDiff < 0) {
            minutesDiff = 60 + minutesDiff;
            hourDiff--;
        }
        if (hourDiff < 0) {
            hourDiff = 24 + hourDiff;
        }
        return (hourDiff * 60) + minutesDiff;
    }

    public static void sethorario(Student data) {
        ProximaAula.data = data;

    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {


        long interval = 1000*60;
        Log.d("Widget", "Vou atualizar");



        // Construct the RemoteViews object
         ProximaAula.views = new RemoteViews(context.getPackageName(), R.layout.proxima_aula);

        //Obtem o horario
        if (data == null) {
            Log.d("Widget", "Lista vazia");
        } else {
            Log.d("Widget", "Vou atualizar o UI");
           updateUI();
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
        Log.d("Widget" , "Vou atualizar em + " + interval);
        alarmManager.setRepeating(AlarmManager.RTC, 5000, 60000 , service);
        //onclick refresh
        views.setOnClickPendingIntent(R.id.class_hour_start, service);
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

