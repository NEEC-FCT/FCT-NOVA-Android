package com.fct.neec.oficial;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.entities.StudentClass;
import com.fct.neec.oficial.ClipRequests.entities.StudentScheduleClass;
import com.fct.neec.oficial.ClipRequests.settings.ClipSettings;
import com.fct.neec.oficial.Fragments.ScheduleFragment;

import org.jsoup.safety.Whitelist;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Implementation of App Widget functionality.
 */
public class ProximaAula extends AppWidgetProvider  {

    private static Student data;
    private static String fileName = "Horario";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Log.d("Widget" , "Vou atualizar");
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.proxima_aula);

        //Obtem o horario
        if (data == null){
            Log.d("Widget" , "Lista vazia");
        }
         
        else {

            Map<Integer, List<StudentScheduleClass>> horario = data.getScheduleClasses();
            Calendar c = Calendar.getInstance();
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            if(horario.containsKey(dayOfWeek)){
                //Vamos mostrar
                List<StudentScheduleClass> diaAtual = horario.get(dayOfWeek);
                Iterator<StudentScheduleClass> it = diaAtual.iterator();
                while (it.hasNext()){
                    StudentScheduleClass aula = it.next();
                    Log.d("Widget" , aula.getName() + " " + aula.getHourStart());

                    //Dar os nomes no widget
                    views.setTextViewText(R.id.class_name, aula.getName());
                    views.setTextViewText(R.id.class_hour_start, aula.getHourStart());
                    views.setTextViewText(R.id.class_hour_end, aula.getHourEnd());
                    views.setTextViewText(R.id.class_room, aula.getRoom());
                }
            }


        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void sethorario(Student data){
        ProximaAula.data = data;


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

