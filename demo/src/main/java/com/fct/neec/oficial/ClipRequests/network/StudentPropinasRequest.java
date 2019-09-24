package com.fct.neec.oficial.ClipRequests.network;

import android.content.Context;
import android.util.Log;

import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.entities.StudentCalendar;
import com.fct.neec.oficial.ClipRequests.exceptions.ServerUnavailableException;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.List;

public class StudentPropinasRequest extends Request {

    private static final String Base = "https://clip.unl.pt/utente/eu/aluno/acto_administrativo/pagamentos_acad%E9micos/propinas/refer%EAncias?tipo_de_per%EDodo_escolar=a&ano_lectivo=";
    private static final String Escola = "&per%EDodo_escolar=1&aluno=";
    private static final String Instituicao = "&institui%E7%E3o=97747";

    public static void getPropinas(Context mContext, String studentNumberId,
                                       String year)
            throws ServerUnavailableException {

        String url = Base + year + Escola + studentNumberId + Instituicao;

        Log.d("Propinas" , "URL is " + url);

        Elements exams = request(mContext, url)
                .body()
                .select("tr[class=vdisplay]");

        Log.d("Propinas" , exams.toString());
        for (Element exam : exams) {
            Log.d("Propinas" , exam.toString());
        }

    }


}
