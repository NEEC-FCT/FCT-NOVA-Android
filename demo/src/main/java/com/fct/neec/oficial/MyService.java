package com.fct.neec.oficial;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.fct.neec.oficial.ClipRequests.entities.StudentScheduleClass;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyService extends Service {
    static RemoteViews view;
    private static StudentScheduleClass melhor;

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

    @Override
    public void onCreate() {
        view = new RemoteViews(getPackageName(), R.layout.proxima_aula);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new SyncData().execute();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    private class SyncData extends AsyncTask<String, Void, String> {

        int total, wicket;
        ComponentName thisWidget;
        AppWidgetManager manager;

        public SyncData() {
            thisWidget = new ComponentName(MyService.this, ProximaAula.class);
            manager = AppWidgetManager.getInstance(MyService.this);
        }

        @Override
        protected void onPreExecute() {
            //  view.setViewVisibility(R.id.progressBar, 0);
            // view.setProgressBar(R.id.progressBar, 0, 0, false);
            manager.updateAppWidget(thisWidget, view);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = "Set your service json get URL here";

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            /*
             * Update widget UI like below sample, replace with your code*/
            Log.d("Widget", "Called");
            if (ProximaAula.data != null) {
                Map<Integer, List<StudentScheduleClass>> horario = ProximaAula.data.getScheduleClasses();
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

                    //atualiza com o melhor

                    // make operation on UI - on example
                    // on progress bar.
                    Log.d("Widget", "Correr na main");
                    view.setTextViewText(R.id.class_name, melhor.getName());
                    view.setTextViewText(R.id.class_hour_start, melhor.getHourStart());
                    view.setTextViewText(R.id.class_hour_end, melhor.getHourEnd());
                    view.setTextViewText(R.id.class_room, melhor.getRoom());
                    Intent configIntent = new Intent(getApplicationContext(), LoadingActivity.class);
                    PendingIntent configPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, configIntent, 0);
                    view.setOnClickPendingIntent(R.id.proxima_aula, configPendingIntent);

                }

            } else {
                view = new RemoteViews(getPackageName(), R.layout.sem_aulas);
                Intent configIntent = new Intent(getApplicationContext(), LoadingActivity.class);
                PendingIntent configPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, configIntent, 0);
                view.setOnClickPendingIntent(R.id.sem_aula, configPendingIntent);
            }

            manager.updateAppWidget(thisWidget, view);

            stopSelf();
        }
    }
}