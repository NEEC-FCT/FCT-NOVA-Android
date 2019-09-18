package com.fct.neec.oficial.ClipRequests.network;

import android.content.Context;

import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.entities.StudentCalendar;
import com.fct.neec.oficial.ClipRequests.exceptions.ServerUnavailableException;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.List;

public class StudentPropinasRequest extends Request {

    private static final String Base = "https://clip.unl.pt/utente/eu/aluno/acto_administrativo/pagamentos_acad%E9micos/propinas/\n" +
            "refer%EAncias?tipo_de_per%EDodo_escolar=a&";
    private static final String STUDENT_CALENDAR_EXAM_2 = "&aluno=";
    private static final String STUDENT_CALENDAR_EXAM_3 = "&institui%E7%E3o=97747&tipo_de_per%EDodo_lectivo=s&per%EDodo_lectivo=";
    private static final String STUDENT_CALENDAR_EXAM_3_TRIMESTER = "&institui%E7%E3o=97747&tipo_de_per%EDodo_lectivo=t&per%EDodo_lectivo=";



    public static void getPropinas(Context mContext, Student student, String studentNumberId,
                                       String year, int semester)
            throws ServerUnavailableException {

        String url = STUDENT_CALENDAR_EXAM_1 + year + STUDENT_CALENDAR_EXAM_2 + studentNumberId;
        if (semester == 3) // Trimester
            url += STUDENT_CALENDAR_EXAM_3_TRIMESTER + (semester - 1);
        else
            url += STUDENT_CALENDAR_EXAM_3 + semester;

        Elements exams = request(mContext, url)
                .body()
                .select("tr[class=texto_tabela]");

        for (Element exam : exams) {
            String name = exam.child(0).text();
            Elements recurso = exam.child(2).select("tr");

            if (recurso.first() == null) continue;

            String date = recurso.first().child(0).text();
            //String url = recurso.first().child(1) //.get(1).child(0).attr("href");
            String hour = recurso.get(1).child(0).child(0).text();

            StudentCalendar calendarAppointement = new StudentCalendar();
            calendarAppointement.setName(name);
            calendarAppointement.setDate(date);
            calendarAppointement.setHour(hour);

            student.addStudentCalendarAppointment(true, calendarAppointement);
        }

    }


}
