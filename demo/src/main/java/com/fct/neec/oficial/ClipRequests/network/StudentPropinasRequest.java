package com.fct.neec.oficial.ClipRequests.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.fct.neec.oficial.ClipRequests.entities.StudenPropinas;
import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.entities.StudentCalendar;
import com.fct.neec.oficial.ClipRequests.exceptions.ServerUnavailableException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentPropinasRequest extends Request {

    private static final String Base = "https://clip.unl.pt/utente/eu/aluno/acto_administrativo/pagamentos_acad%E9micos/propinas/refer%EAncias?tipo_de_per%EDodo_escolar=a&ano_lectivo=";
    private static final String Escola = "&per%EDodo_escolar=1&aluno=";
    private static final String Instituicao = "&institui%E7%E3o=97747";

    public static void getPropinas(Context mContext , String studentNumberId,
                                       String year)
            throws ServerUnavailableException {

        String url = Base + year + Escola + studentNumberId + Instituicao;

        Log.d("Propinas" , "URL is " + url);

        Elements linhas = request(mContext, url)
                .body().select("tbody").select("tr");

        for (Element linha : linhas){
            if(linha.childNodeSize() == 8  ) {
                if (linha.child(1).text().contains("prestação") || linha.child(1).text().contains("Pagamento")) {

                    Log.d("URL" , linha.child(1).children().attr("href"));
                    Log.d("Nome" , linha.child(1).text());
                    Log.d("Pagamento" , linha.child(2).text());
                    Log.d("Data" , linha.child(3).text());

                    String URL = linha.child(1).children().attr("href");
                    String nome =  linha.child(1).text().trim();
                    String pagamento =  linha.child(2).text().trim();
                    String data =  linha.child(3).text().trim();
                    StudenPropinas info = new StudenPropinas(nome);
                    info.setMontante(pagamento);
                    info.setData(data);
                    info.setURL(URL);
                    info.setNome(nome);
                    HashMap<String, StudenPropinas> mapa = getHashMap(mContext);
                    if(mapa == null)
                        mapa = new HashMap<String, StudenPropinas>();
                    mapa.put(URL,info);
                    saveHashMap(mapa,mContext);


                }
            }

        }

    }

    public static void saveHashMap( Object obj , Context mContext ) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        editor.putString("MB",json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public static HashMap<String,StudenPropinas> getHashMap( Context mContext) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = prefs.getString("MB","");
        java.lang.reflect.Type type = new TypeToken<HashMap<String,StudenPropinas>>(){}.getType();
        HashMap<String,StudenPropinas> obj = gson.fromJson(json, type);
        return obj;
    }
}
