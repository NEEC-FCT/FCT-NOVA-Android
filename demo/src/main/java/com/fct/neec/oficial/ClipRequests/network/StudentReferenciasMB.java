package com.fct.neec.oficial.ClipRequests.network;

import android.content.Context;
import android.util.Log;

import com.fct.neec.oficial.ClipRequests.exceptions.ServerUnavailableException;
import com.fct.neec.oficial.ClipRequests.network.Request;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class StudentReferenciasMB extends Request {


    private static String Base = "https://clip.unl.pt";
    public static void GetStudentReferencias(Context mContext, String url)
            throws ServerUnavailableException {


       // Log.d("Referencias" , "URL is " +  Base + url.trim());

        Elements all = request(mContext, Base + url.trim())
                .body().select("tbody");
        Elements tabelaDados = all.get(all.size() - 2).select("tr");
       // Log.d( "Referencias" , tabelaDados.text());
        Log.d( "Referencias" , "Entidade -> " + tabelaDados.get(1).select("td").text());
        Log.d( "Referencias" , "Referência -> " + tabelaDados.get(2).select("td").text());
        Log.d( "Referencias" , "Montante -> " + tabelaDados.get(3).select("td").text());




    }
}
