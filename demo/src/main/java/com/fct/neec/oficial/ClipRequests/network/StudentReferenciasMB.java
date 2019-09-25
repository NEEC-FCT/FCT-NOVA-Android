package com.fct.neec.oficial.ClipRequests.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.fct.neec.oficial.ClipRequests.entities.StudenPropinas;
import com.fct.neec.oficial.ClipRequests.entities.Student;
import com.fct.neec.oficial.ClipRequests.exceptions.ServerUnavailableException;
import com.fct.neec.oficial.ClipRequests.network.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

public class StudentReferenciasMB extends Request {


    private static String Base = "https://clip.unl.pt";

    public static void GetStudentReferencias(Context mContext , String url)
            throws ServerUnavailableException {


       // Log.d("Referencias" , "URL is " +  Base + url.trim());

        Elements all = request(mContext, Base + url.trim())
                .body().select("tbody");
        Elements tabelaDados = all.get(all.size() - 2).select("tr");
       // Log.d( "Referencias" , tabelaDados.text());
        StudenPropinas atualizar = getHashMap(mContext).get(url.trim());
        Log.d( "Referencias" , "Entidade -> " + tabelaDados.get(1).select("td").text());
        Log.d( "Referencias" , "Referência -> " + tabelaDados.get(2).select("td").text());
        Log.d( "Referencias" , "Montante -> " + tabelaDados.get(3).select("td").text());

        String entidade =  tabelaDados.get(1).select("td").text();
        String referencia =  tabelaDados.get(2).select("td").text();
        String montante =  tabelaDados.get(3).select("td").text();

        atualizar.setEntidade(entidade);
        atualizar.setReferencia(referencia);
        atualizar.setMontante(montante);
        HashMap<String, StudenPropinas> mapa = getHashMap(mContext);
        mapa.put(url.trim(),atualizar);
        saveHashMap(mapa,mContext);


    }

    public static void saveHashMap( Object obj , Context mContext ) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        editor.putString("MB",json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public static HashMap<String,StudenPropinas> getHashMap(Context mContext) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = prefs.getString("MB","");
        java.lang.reflect.Type type = new TypeToken<HashMap<String,StudenPropinas>>(){}.getType();
        HashMap<String,StudenPropinas> obj = gson.fromJson(json, type);
        return obj;
    }
}
